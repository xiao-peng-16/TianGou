package com.cxp.shop_order.service.impl;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.entity.AddMultipleOrderParent;
import com.cxp.shop_api.entity.AddSingleOrderParent;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.OrderCommodityVO;
import com.cxp.shop_api.vo.StoreStatusFullVO;
import com.cxp.shop_order.eception.CommodityNotFound_exception;
import com.cxp.shop_order.eception.CommodityStockInsufficientException;
import com.cxp.shop_order.mapper.OrderIdKeyMapper;
import com.cxp.shop_order.mapper.OrderMapper;
import com.cxp.shop_order.service.FeignClient.CommodityFeignClient;
import com.cxp.shop_order.service.FeignClient.StoreFeignClient;
import com.cxp.shop_order.service.FeignClient.UserFeignClient;
import com.cxp.shop_order.service.OrderService;
import com.cxp.shop_order.utils.SubmitOrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderIdKeyMapper orderIdKeyMapper;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    CommodityFeignClient commodityFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;


    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean ORDER_NOT_FIND =ResultFactory.createFailResult(ResultStatus.ORDER_NOT_FIND);
    static final ResultBean ORDER_SUBMIT_ERROR =ResultFactory.createFailResult(ResultStatus.ORDER_SUBMIT_ERROR);

    static final CommodityNotFound_exception COMMODITY_NOT_FOUND_EXCEPTION = new CommodityNotFound_exception();
    static final CommodityStockInsufficientException COMMODITY_STOCK_INSUFFICIENT_EXCEPTION = new CommodityStockInsufficientException();





    @Transactional(rollbackFor = Exception.class)
    @Override
    public int submitSingleOrder(Integer userId, OrderSon orderSon) throws CommodityNotFound_exception, CommodityStockInsufficientException {

        CommodityToOrder commodityToOrder = commodityFeignClient.getCommodityToOrder(userId, orderSon.getCommodityId());


        //找不到任何商品信息 商品信息一个都找不到
        if (null == commodityToOrder)
            throw COMMODITY_NOT_FOUND_EXCEPTION;
        //直接从商品页购买 或购物车只有一件商品
        int purchaseQuantity = orderSon.getPurchaseQuantity();
        double commodityPrice = commodityToOrder.getCommodityPrice();
        if (commodityToOrder.getCommodityStock() < purchaseQuantity)
            throw  COMMODITY_STOCK_INSUFFICIENT_EXCEPTION;

        AddSingleOrderParent addSingleOrderParent = new AddSingleOrderParent(userId, commodityToOrder.getStoreId());
        addSingleOrderParent.setOrderTotalQuantity(purchaseQuantity);
        addSingleOrderParent.setOrderTotalPrice(purchaseQuantity * commodityPrice);

        orderMapper.insOrderParent(addSingleOrderParent);
        int orderId = addSingleOrderParent.getOrderId();

        orderSon.setOrderId(orderId);
        orderSon.setCommodityPrice(commodityPrice);
        orderSon.setPurchaseQuantity(purchaseQuantity);
        orderMapper.insOrderSon(orderSon);

        return orderId;
    }

    @Override
    public List<Integer> submitMultipleOrder(Integer userId, List<OrderSon> orderSonList) throws CommodityNotFound_exception, CommodityStockInsufficientException {

        List<Integer> commodityIdList = orderSonList.stream().map(e -> e.getCommodityId()).collect(Collectors.toList());
        Map<Integer, CommodityToOrder> commodityIdToOrderMap = commodityFeignClient.mapCommodityToOrder(userId, commodityIdList);

        //找不到任何商品信息 商品信息一个都找不到
        if (0 ==commodityIdToOrderMap.size())
            throw COMMODITY_NOT_FOUND_EXCEPTION;

        SubmitOrderUtils submitOrderUtils = new SubmitOrderUtils(userId);
        for ( OrderSon orderSon : orderSonList) {
            CommodityToOrder commodityToOrder = commodityIdToOrderMap.get(orderSon.getCommodityId());
            if (commodityToOrder == null)
                orderSonList.remove(orderSon);  //商品信息都找不到 剔除该条
            else if (commodityToOrder.getCommodityStock() < orderSon.getPurchaseQuantity())
                orderSonList.remove(orderSon);  //商品库存不足 剔除该条
            submitOrderUtils.work(orderSon, commodityToOrder);
        }
        if (0 ==orderSonList.size())          //购物车中 剩下 商品库存都不足
            throw  COMMODITY_STOCK_INSUFFICIENT_EXCEPTION;

        List<AddMultipleOrderParent> addMultipleOrderParentList = submitOrderUtils.popResult().entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
        //设置orderSon 的订单号
        orderMapper.inOrderParentList(addMultipleOrderParentList);
        addMultipleOrderParentList.stream().forEach(e -> {
            int orderId = e.getOrderId();
            e.getOrderSonList().stream().forEach(orderSon -> orderSon.setOrderId(orderId));
        });
        orderMapper.insOrderSonList(orderSonList);

        return addMultipleOrderParentList.stream().map(e -> e.getOrderId()).collect(Collectors.toList());
    }




    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBean payOrderByUserId(int userId, List<Integer> orderIdList) {


        if (0 ==orderIdList.size())
            return ORDER_NOT_FIND;

        //转账
        LinkedList<MoneyChange> moneyChangeLinkedList = orderMapper.ListMoneyChangeByuserIdOrderId(userId, orderIdList);
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


    //粗略订单

    //返回 订单id -> List<OrderCommodityVO>
    private   Map<Integer, List<OrderCommodityVO>> mapOrderId_OrderCommodityVOList(List<OrderParent> orderParentList){
        //每条订单涉及的商品信息
        Map<Integer, List<Integer>> orderId_CommodityIdMap = orderIdKeyMapper.getOrderIdCommodityIdMap(orderParentList);
        List<Integer> commodityIdList = orderId_CommodityIdMap.entrySet().stream().map(e -> e.getValue())
                .flatMap(List::stream).distinct().collect(Collectors.toList());
        // key => 商品id  value => 商品信息
        Map<Integer, OrderCommodityVO> commodityId_OrderCommodityVOMap = commodityFeignClient.mapOrderCommodityVO(commodityIdList);
        commodityIdList = null;
        // key => 订单id  value => 该订单前3个商品信息 的list
        Map<Integer, List<OrderCommodityVO>> orderId_OrderCommodityVOList_Map = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : orderId_CommodityIdMap.entrySet()){
            List<Integer> cIdList = entry.getValue();
            List<OrderCommodityVO> simpleCommodityList = new ArrayList<>();
            for (Integer cId : cIdList)
                simpleCommodityList.add(commodityId_OrderCommodityVOMap.get(cId));
            orderId_OrderCommodityVOList_Map.put(entry.getKey(),simpleCommodityList);
        }
        return  orderId_OrderCommodityVOList_Map;
    }

    @Override
    public List<OrderParent> listStoreOrderParentRough(Integer storeId) {

        List<OrderParent> orderParentList = orderMapper.listStoreOrderParent(storeId);
        if (0 == orderParentList.size()) return null;

        Map<Integer, List<OrderCommodityVO>> orderId_OrderCommodityVOList_Map = mapOrderId_OrderCommodityVOList(orderParentList);

        //查询买家用户名 根据userId
        List<Integer> userIdList = orderParentList.stream().map(e ->e.getUserId()).distinct().collect(Collectors.toList());
        Map<Integer, String> userNameByUserId = userFeignClient.mapUserNameByUserId(userIdList);

        for (OrderParent orderParent : orderParentList){
            orderParent.setUserName(userNameByUserId.get(orderParent.getUserId()));
            orderParent.setOrderCommodityVOList(orderId_OrderCommodityVOList_Map.get(orderParent.getOrderId()));
        }
        return orderParentList;
    }

    @Override
    public List<OrderParent> listUserOrderParentRough(Integer userId, Integer orderState) {

        List<OrderParent> orderParentList = orderMapper.listUserOrderParent(userId, orderState);
        if (0 == orderParentList.size()) return null;

        Map<Integer, List<OrderCommodityVO>> orderId_OrderCommodityVOList_Map = mapOrderId_OrderCommodityVOList(orderParentList);

        //查询买家用户名 根据userId
        List<Integer> storeIdList = orderParentList.stream().map(e ->e.getStoreId()).distinct().collect(Collectors.toList());
        Map<Integer, String> storeNameByStoreId = storeFeignClient.mapStoreNameByStoreId(storeIdList);

        for (OrderParent orderParent : orderParentList){
            orderParent.setStoreName(storeNameByStoreId.get(orderParent.getStoreId()));
            orderParent.setOrderCommodityVOList(orderId_OrderCommodityVOList_Map.get(orderParent.getOrderId()));
        }
        return orderParentList;
    }


    //详细订单
    private OrderParent getOrderParent(Integer orderId) {

        OrderParent orderParent = orderMapper.getStoreOrderParent(orderId);
        if(null == orderParent) return null;
        //设置 买家  卖家 名字
        orderParent.setUserName(userFeignClient.getUserNameByUserId(orderParent.getUserId()));
        orderParent.setStoreName(storeFeignClient.getStoreNameByStoreId(orderParent.getStoreId()));

        List<Integer> commodityIdList = orderParent.getOrderSonList().stream().map(e -> e.getCommodityId()).collect(Collectors.toList());
        Map<Integer, OrderCommodityVO> commodityId_OrderCommodityVOMap = commodityFeignClient.mapOrderCommodityVO(commodityIdList);
        for (OrderSon orderSon : orderParent.getOrderSonList())
            orderSon.setOrderCommodityVO( commodityId_OrderCommodityVOMap.get( orderSon.getCommodityId()));
        return orderParent;
    }

    @Override
    public OrderParent getStoreOrderParent(Integer storeId, Integer orderId) {
        OrderParent orderParent = getOrderParent(orderId);
        if (null == orderParent || storeId != orderParent.getStoreId())
            return null;
        return orderParent;
    }

    @Override
    public OrderParent getUserOrderParent(Integer userId, Integer orderId) {
        OrderParent orderParent = getOrderParent(orderId);
        if (null == orderParent || userId != orderParent.getUserId())
            return null;
        return orderParent;
    }



}
