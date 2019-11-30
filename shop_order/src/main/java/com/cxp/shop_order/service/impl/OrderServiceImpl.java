package com.cxp.shop_order.service.impl;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.OrderCommodityVO;
import com.cxp.shop_api.vo.OrderGeneraVO;
import com.cxp.shop_api.vo.StoreStatusFullVO;
import com.cxp.shop_order.eception.AddOrderException;
import com.cxp.shop_order.mapper.OrderIdKeyMapper;
import com.cxp.shop_order.mapper.OrderMapper;
import com.cxp.shop_order.service.FeignClient.CommodityFeignClient;
import com.cxp.shop_order.service.FeignClient.UserFeignClient;
import com.cxp.shop_order.service.OrderService;
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


    static final AddOrderException ADD_ORDER_EXCEPTION = new AddOrderException();
    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean ORDER_ID_ERROR =ResultFactory.createFailResult(ResultStatus.ORDER_ID_ERROR);
    static final ResultBean ORDER_ID_USER_ID_MISMATCHING =ResultFactory.createFailResult(ResultStatus.ORDER_ID_USER_ID_MISMATCHING);
    static final ResultBean ORDER_SUBMIT_ERROR =ResultFactory.createFailResult(ResultStatus.ORDER_SUBMIT_ERROR);
    @Transactional(rollbackFor = AddOrderException.class)
    @Override
    public ResultBean addOrder(OrderParent orderParent) throws AddOrderException {
        if ( 0 ==orderMapper.insOrderParent(orderParent))
            throw ADD_ORDER_EXCEPTION;
        int orderId = orderParent.getOrderId(); //mybatis返回注入 自增id
        List<OrderSon> orderSonList = orderParent.getOrderSonList();
        if (orderSonList.size() != orderMapper.insOrderSon(orderId,orderSonList))
            throw ADD_ORDER_EXCEPTION;
        return ResultFactory.createSuccessResult(orderId);
    }

    @Override
    public ResultBean payOrderByUserId(int userId,int orderId) {

        MoneyChange userMoneyChange = orderMapper.selUserMoneyChangeByOrderId(orderId);
        if (userMoneyChange == null)     //订单id不对 或 该订单已经支付
            return ORDER_ID_ERROR;
        if (userId != userMoneyChange.getUserId())  ////订单id 和 买家id不匹配
            return ORDER_ID_USER_ID_MISMATCHING;
        //转账
        LinkedList<MoneyChange> moneyChangeLinkedList = orderMapper.selAllMoneyChangeByOrderId(orderId);
        userMoneyChange.setUserMoneyChange(-userMoneyChange.getUserMoneyChange());
        moneyChangeLinkedList.addFirst(userMoneyChange);
        ResultBean resultBean = userFeignClient.shopTransferByUserId(moneyChangeLinkedList);

        //刷新支付状态
        if (resultBean.isSuccess()){
            if (0 == orderMapper.updSubmintByOrderId(orderId))   //刷新支付状态
                return ORDER_SUBMIT_ERROR;
            //修改商品库存 销量
            List<CommodityNumberChange> commodityNumberChangeList = orderMapper.selChooseNumber(orderId);
            resultBean = commodityFeignClient.updCommodityNumber(commodityNumberChangeList);
        }
        return resultBean;
    }



    @Override
    public StoreStatusFullVO selStoreStatusFullVO(Integer storeId) {
        StoreStatusFullVO fullStoreSales = new StoreStatusFullVO();
        fullStoreSales.setCurrentMonth(orderMapper.selStoreSalesCurrentMonth(storeId));
        fullStoreSales.setTotality(orderMapper.selStoreSalesEarnings(storeId));
        return fullStoreSales;
    }

    @Override
    public List<OrderGeneraVO> listStoreOrderGeneraVO(Integer storeId) {
        List<OrderGeneraVO> orderGeneraList = orderMapper.listStoreOrderGeneraVO(storeId);
        Map<Integer, OrderParent> orderParentMap = orderMapper.selOrderParentMap(orderGeneraList);

        //查询买家用户名 根据userId
        List<Integer> userIdList = orderParentMap.entrySet().stream().map(e ->e.getValue().getUserId()).distinct().collect(Collectors.toList());
        Map<Integer, String> userNameByUserId = userFeignClient.mapUserNameByUserId(userIdList);

        //每条订单涉及的商品信息，每条订单商品信息不多于4个
        // key => 订单id  value => 该订单前3个商品id 的list
        Map<Integer, List<Integer>> orderId_CommodityIdMap = orderIdKeyMapper.getOrderIdCommodityIdMap(storeId,orderGeneraList);
        List<Integer> commodityIdList = orderId_CommodityIdMap.entrySet().stream().map(e -> e.getValue())
                                            .flatMap(List::stream).distinct().collect(Collectors.toList());
        // key => 商品id  value => 商品信息
        Map<Integer, OrderCommodityVO> commodityId_OrderCommodityVOMap = commodityFeignClient.mapOrderCommodityVO(commodityIdList);
        commodityIdList = null;
        // key => 订单id  value => 该订单前3个商品信息 的list
        Map<Integer, List<OrderCommodityVO>> orderId_OrderCommodityVOMap = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : orderId_CommodityIdMap.entrySet()){
            List<Integer> cIdList = entry.getValue();
            List<OrderCommodityVO> simpleCommodityList = new ArrayList<>();
            for (Integer cId : cIdList)
                simpleCommodityList.add(commodityId_OrderCommodityVOMap.get(cId));
            orderId_OrderCommodityVOMap.put(entry.getKey(),simpleCommodityList);
        }
        orderId_CommodityIdMap = null;
        commodityId_OrderCommodityVOMap = null;

        for (OrderGeneraVO orderGenera : orderGeneraList){
            OrderParent orderParent = orderParentMap.get(orderGenera.getOrderId());
            orderParent.setUserName(userNameByUserId.get(orderParent.getUserId()));
            orderGenera.setOrderParent(orderParent);

            orderGenera.setOrderCommodityVOList(orderId_OrderCommodityVOMap.get(orderGenera.getOrderId()));
        }
        return orderGeneraList;
    }

    @Override
    public OrderParent selStoreOrderParent(Integer userId, Integer orderId) {
        OrderParent orderParent = orderMapper.selStoreOrderParent(userId, orderId);
        //函数参数的userId 是店铺 卖家的id  ，orderParent里的userId才是买家的
        orderParent.setUserName(userFeignClient.selUserNameByUserId(orderParent.getUserId()));
        List<Integer> commodityIdList = orderParent.getOrderSonList().stream().map(e -> e.getCommodityId()).collect(Collectors.toList());
        Map<Integer, OrderCommodityVO> commodityId_OrderCommodityVOMap = commodityFeignClient.mapOrderCommodityVO(commodityIdList);
        for (OrderSon orderSon : orderParent.getOrderSonList())
            orderSon.setOrderCommodityVO( commodityId_OrderCommodityVOMap.get( orderSon.getCommodityId()));

        return orderParent;
    }

    @Override
    public List<OrderGeneraVO> listUserOrderGeneraVO(Integer userId) {
        return null;
    }




}
