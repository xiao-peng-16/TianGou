package com.cxp.shop_order.service.impl;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.StoreStatusFullVO;
import com.cxp.shop_order.eception.CommodityNotFound_exception;
import com.cxp.shop_order.eception.CommodityStockInsufficientException;
import com.cxp.shop_order.mapper.OrderMapper;
import com.cxp.shop_order.pojo.*;
import com.cxp.shop_order.rabbit.RabbitConfig;
import com.cxp.shop_order.service.FeignClient.CommodityFeignClient;
import com.cxp.shop_order.service.FeignClient.StoreFeignClient;
import com.cxp.shop_order.service.FeignClient.UserFeignClient;
import com.cxp.shop_order.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RabbitTemplate rabbitTemplate;
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
    static final ResultBean ORDER_STOCK_LOCK_EXPIRATION =ResultFactory.createFailResult(ResultStatus.ORDER_STOCK_LOCK_EXPIRATION);

    static final CommodityNotFound_exception COMMODITY_NOT_FOUND_EXCEPTION = new CommodityNotFound_exception();
    static final CommodityStockInsufficientException COMMODITY_STOCK_INSUFFICIENT_EXCEPTION = new CommodityStockInsufficientException();





    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBean submitSingleOrder(Integer userId, PurchaseDTO purchaseDTO) {

        AddOrderSonResultBean addOrderSonResultBean = commodityFeignClient.getCommodityToOrder(userId, purchaseDTO);

        if (!addOrderSonResultBean.isSuccess()){
            return ResultFactory.createFailResult(addOrderSonResultBean.getStatus());
        }


        AddOrderSon addOrderSon = addOrderSonResultBean.getData();
        int storeId = addOrderSon.getStoreId();
        int purchaseQuantity = addOrderSon.getPurchaseQuantity();

        Date date = new Date();
        Long orderId = Long.parseLong(((""+date.getTime())+userId) + storeId);

        AddSingleOrderParent addSingleOrderParent = new AddSingleOrderParent();
        addSingleOrderParent.setOrderId(orderId)
                .setOrderTime(dateFormat.format(date))
                .setStockLockExpiration(System.currentTimeMillis() + RabbitConfig.EXPIRATION_LONG)
                .setUserId(userId)
                .setUserName(userFeignClient.getUserNameByUserId(userId))
                .setStoreId(storeId)
                .setStoreName(storeFeignClient.getStoreNameByStoreId(storeId));

        addSingleOrderParent.setOrderTotalQuantity(purchaseQuantity);
        addSingleOrderParent.setOrderTotalPrice(purchaseQuantity * addOrderSon.getCommodityPrice());


        orderMapper.insOrderParent(addSingleOrderParent);

        addOrderSon.setOrderId(orderId);
        orderMapper.insOrderSon(addOrderSon);

        //验证队列 支付过期 进行库存预占回退
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_DELAY_EXCHANGE, RabbitConfig.ORDER_DELAY_ROUTING_KEY
                , orderId, message -> {
                    message.getMessageProperties().setExpiration(RabbitConfig.EXPIRATION_STRING);
                    return message;
                });

        return ResultFactory.createSuccessResult(orderId);
    }

    @Override
    public ResultBean submitMultipleOrder(Integer userId, List<PurchaseDTO> purchaseDTOList){

        Date date = new Date();
        String orderTime = dateFormat.format(date);
        String orderIdPre = ""+date.getTime()+userId;

        //获取商品信息
        AddOrderListSonResultBean addOrderListSonResultBean = commodityFeignClient.listCommodityToOrder(userId, purchaseDTOList);
        if (!addOrderListSonResultBean.isSuccess()){
            return ResultFactory.createFailResult(addOrderListSonResultBean.getStatus());
        }


        List<AddOrderSon> addOrderSonList = addOrderListSonResultBean.getData();

        //按店铺id 分组 最终不同店铺 形成多份订单
        Map<Integer, List<AddOrderSon>> storeId_addOrderSonMap = addOrderSonList.stream().collect(Collectors.groupingBy(e -> e.getStoreId()));
        //获取店铺名
        Map<Integer, String> storeId_storeNameMap = storeFeignClient.mapStoreNameByStoreId(storeId_addOrderSonMap.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList()));

        ArrayList<AddMultipleOrderParent> addMultipleOrderParents = new ArrayList<>();
        storeId_addOrderSonMap.entrySet().stream().forEach(e->{

            Integer storeId = e.getKey();
            Long orderId = Long.parseLong(orderIdPre+storeId);

            //生成订单父表
            AddMultipleOrderParent addMultipleOrderParent = new AddMultipleOrderParent();
            addMultipleOrderParent.setOrderId(orderId)
                    .setStoreId(storeId)
                    .setStoreName(storeId_storeNameMap.get(storeId))
                    .setOrderTotalQuantity(e.getValue().stream().mapToInt(AddOrderSon::getPurchaseQuantity).sum());

            e.getValue().stream().forEach(item->{
                double addPrice = item.getPurchaseQuantity() * item.getCommodityPrice();
                addMultipleOrderParent.setOrderTotalPrice(addPrice + addMultipleOrderParent.getOrderTotalPrice());
                item.setOrderId(orderId);
            });
            addMultipleOrderParents.add(addMultipleOrderParent);
        });

        orderMapper.inOrderParentList(new AddMultipleOrderParentToTal(userId, userFeignClient.getUserNameByUserId(userId)
                , orderTime, System.currentTimeMillis() + RabbitConfig.EXPIRATION_LONG, addMultipleOrderParents));
        orderMapper.insOrderSonList(addOrderSonList);

        List<Long> orderIdList = addMultipleOrderParents.stream().map(e -> e.getOrderId()).collect(Collectors.toList());

        //验证队列 支付过期 进行库存预占回退
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_DELAY_EXCHANGE, RabbitConfig.ORDER_DELAY_ROUTING_KEY
                , orderIdList, message -> {
                    message.getMessageProperties().setExpiration(RabbitConfig.EXPIRATION_STRING);
                    return message;
                });


        return ResultFactory.createSuccessResult(orderIdList);
    }




    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBean payOrderByUserId(int userId, List<Long> orderIdList) {


        List<OrderPayPO> orderPayPOList = orderMapper.listOrderPayPO(userId, orderIdList);

        Map<Integer, Integer> storeId_userId = storeFeignClient.mapUserIdByStoreId(orderPayPOList.stream().map(e -> e.getStoreId()).collect(Collectors.toList()));

        if (orderPayPOList.size() != orderIdList.size()){
            return ORDER_NOT_FIND;
        }

        for (OrderPayPO orderPayPO : orderPayPOList){
            if (!orderPayPO.isStockLockState())
                return ORDER_STOCK_LOCK_EXPIRATION;
            orderPayPO.setUserId( storeId_userId.get(orderPayPO.getStoreId()) );
        }

        ResultBean resultBean = userFeignClient.shopTransferByUserId(userId, orderPayPOList);

        //刷新支付状态
        if (resultBean.isSuccess()){
            if (0 == orderMapper.updOrderStateByUserIdOrderId(userId, orderIdList))   //刷新支付状态
                return ORDER_SUBMIT_ERROR;
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
