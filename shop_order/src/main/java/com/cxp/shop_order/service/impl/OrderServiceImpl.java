package com.cxp.shop_order.service.impl;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.StoreStatusFullVO;
import com.cxp.shop_order.eception.CommodityNotFound_exception;
import com.cxp.shop_order.eception.CommodityStockInsufficientException;
import com.cxp.shop_order.mapper.OrderMapper;
import com.cxp.shop_order.pojo.AddMultipleOrderParent;
import com.cxp.shop_order.pojo.AddMultipleOrderParentToTal;
import com.cxp.shop_order.pojo.AddOrderSon;
import com.cxp.shop_order.pojo.AddSingleOrderParent;
import com.cxp.shop_order.service.FeignClient.CommodityFeignClient;
import com.cxp.shop_order.service.FeignClient.StoreFeignClient;
import com.cxp.shop_order.service.FeignClient.UserFeignClient;
import com.cxp.shop_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    CommodityFeignClient commodityFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;


    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean ORDER_NOT_FIND =ResultFactory.createFailResult(ResultStatus.ORDER_NOT_FIND);
    static final ResultBean ORDER_SUBMIT_ERROR =ResultFactory.createFailResult(ResultStatus.ORDER_SUBMIT_ERROR);

    static final CommodityNotFound_exception COMMODITY_NOT_FOUND_EXCEPTION = new CommodityNotFound_exception();
    static final CommodityStockInsufficientException COMMODITY_STOCK_INSUFFICIENT_EXCEPTION = new CommodityStockInsufficientException();





    @Transactional(rollbackFor = Exception.class)
    @Override
    public long submitSingleOrder(Integer userId, AddOrderSon addOrderSon) throws CommodityNotFound_exception, CommodityStockInsufficientException {

        Date date = new Date();
        Long orderId = Long.parseLong(""+date.getTime()+userId);

        CommodityToOrder commodityToOrder = commodityFeignClient.getCommodityToOrder(userId, addOrderSon.getCommodityId());

        //找不到任何商品信息 商品信息一个都找不到
        if (null == commodityToOrder)
            throw COMMODITY_NOT_FOUND_EXCEPTION;
        int storeId = commodityToOrder.getStoreId();

        //直接从商品页购买 或购物车只有一件商品
        int purchaseQuantity = addOrderSon.getPurchaseQuantity();
        if (commodityToOrder.getCommodityStock() < purchaseQuantity)
            throw  COMMODITY_STOCK_INSUFFICIENT_EXCEPTION;

        AddSingleOrderParent addSingleOrderParent = new AddSingleOrderParent();
        addSingleOrderParent.setOrderId(orderId)
                .setOrderTime(dateFormat.format(date))
                .setUserId(userId)
                .setUserName(userFeignClient.getUserNameByUserId(userId))
                .setStoreId(storeId)
                .setStoreName(storeFeignClient.getStoreNameByStoreId(storeId));

        addSingleOrderParent.setOrderTotalQuantity(purchaseQuantity);
        addSingleOrderParent.setOrderTotalPrice(purchaseQuantity * commodityToOrder.getCommodityPrice());

        orderMapper.insOrderParent(addSingleOrderParent);

        addOrderSon.setOrderId(orderId);
        addOrderSon.setCommodityToOrder(commodityToOrder);

        orderMapper.insOrderSon(addOrderSon);

        return orderId;
    }

    @Override
    public List<Long> submitMultipleOrder(Integer userId, List<AddOrderSon> addOrderSonList) throws CommodityNotFound_exception, CommodityStockInsufficientException {

        Date date = new Date();
        String orderTime = dateFormat.format(date);
        String orderIdPre = ""+date.getTime()+userId;

        //获取商品信息
        List<Integer> commodityIdList = addOrderSonList.stream().map(e -> e.getCommodityId()).collect(Collectors.toList());
        Map<Integer, CommodityToOrder> commodityIdToOrderMap = commodityFeignClient.mapCommodityToOrder(userId, commodityIdList);

        for (AddOrderSon addOrderSon : addOrderSonList){
            CommodityToOrder commodityToOrder = commodityIdToOrderMap.get(addOrderSon.getCommodityId());
            if (commodityToOrder == null)
                throw COMMODITY_NOT_FOUND_EXCEPTION;  //商品信息都找不到
            else if (commodityToOrder.getCommodityStock() < addOrderSon.getPurchaseQuantity())
                throw  COMMODITY_STOCK_INSUFFICIENT_EXCEPTION;  //商品库存不足
            addOrderSon.setCommodityToOrder(commodityToOrder);
        }
        //按店铺id 分组 最终不同店铺 形成多份订单
        Map<Integer, List<AddOrderSon>> storeId_addOrderSonMap = addOrderSonList.stream().collect(Collectors.groupingBy(e -> e.getCommodityToOrder().getStoreId()));
        //获取店铺名
        Map<Integer, String> storeId_storeNameMap = storeFeignClient.mapStoreNameByStoreId(storeId_addOrderSonMap.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList()));

        ArrayList<AddMultipleOrderParent> addMultipleOrderParents = new ArrayList<>();
        storeId_addOrderSonMap.entrySet().stream().forEach(e->{

            Integer storeId = e.getKey();
            Long orderId = Long.parseLong(orderIdPre+storeId);

            AddMultipleOrderParent addMultipleOrderParent = new AddMultipleOrderParent();
            addMultipleOrderParent.setOrderId(orderId)
                    .setOrderTime(orderTime)
                    .setStoreId(storeId)
                    .setStoreName(storeId_storeNameMap.get(storeId))
                    .setOrderTotalQuantity(e.getValue().stream().mapToInt(AddOrderSon::getPurchaseQuantity).sum());

            e.getValue().stream().forEach(item->{
                double addPrice = item.getPurchaseQuantity() * item.getCommodityToOrder().getCommodityPrice();
                addMultipleOrderParent.setOrderTotalPrice(addPrice + addMultipleOrderParent.getOrderTotalQuantity());
                item.setOrderId(orderId);
            });
            addMultipleOrderParents.add(addMultipleOrderParent);
        });

        orderMapper.inOrderParentList(new AddMultipleOrderParentToTal(userId, userFeignClient.getUserNameByUserId(userId), addMultipleOrderParents));
        orderMapper.insOrderSonList(addOrderSonList);

        return addMultipleOrderParents.stream().map(e -> e.getOrderId()).collect(Collectors.toList());
    }




    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBean payOrderByUserId(int userId, List<Long> orderIdList) {


        if (0 ==orderIdList.size())
            return ORDER_NOT_FIND;

        //转账
        LinkedList<MoneyChange> moneyChangeLinkedList = orderMapper.ListMoneyChangeByUserIdOrderId(userId, orderIdList);
        if (0 == moneyChangeLinkedList.size()){
            return ORDER_NOT_FIND;
        }
        double sumMoneyChange = moneyChangeLinkedList.stream().mapToDouble(e -> e.getUserMoneyChange()).sum();
        moneyChangeLinkedList.addFirst(new MoneyChange(userId, -sumMoneyChange));
        ResultBean resultBean = userFeignClient.shopTransferByUserId(moneyChangeLinkedList);

        //刷新支付状态
        if (resultBean.isSuccess()){
            if (0 == orderMapper.updOrderStateByUserIdOrderId(userId, orderIdList))   //刷新支付状态
                return ORDER_SUBMIT_ERROR;
            //修改商品库存 销量
            List<CommodityNumberChange> commodityNumberChangeList = orderMapper.getPurchaseQuantityByOrderId(userId, orderIdList);
            resultBean = commodityFeignClient.updCommodityNumber(commodityNumberChangeList);
        }
        return resultBean;
    }



    @Override
    public StoreStatusFullVO getStoreStatusFullVO(Integer storeId) {
        StoreStatusFullVO fullStoreSales = new StoreStatusFullVO();
        fullStoreSales.setCurrentMonth(orderMapper.getStoreSalesCurrentMonth(storeId));
        fullStoreSales.setTotality(orderMapper.getStoreSalesEarnings(storeId));
        return fullStoreSales;
    }




    @Override
    public List<OrderParent> listStoreOrderParent(Integer storeId) {
        return orderMapper.listStoreOrderParent(storeId);
    }

    @Override
    public List<OrderParent> listUserOrderParent(Integer userId, Integer orderState) {
        return orderMapper.listUserOrderParent(userId, orderState);
    }


    @Override
    public OrderParent getOrderParent(Integer orderId) {
        return orderMapper.getOrderParent(orderId);
    }



}
