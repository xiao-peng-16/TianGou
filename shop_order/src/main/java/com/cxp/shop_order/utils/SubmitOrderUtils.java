package com.cxp.shop_order.utils;

import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.entity.OrderSon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubmitOrderUtils  {

    static SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Map<Integer, OrderParent> storeId_orderParentMap = new HashMap<>();
    String orderTime = dateFormat.format(new Date());
    int userId;

    public SubmitOrderUtils(int userId) {
        this.userId = userId;
    }




    public void work(OrderSon orderSon, CommodityToOrder commodityToOrder){

        double commodityPrice = commodityToOrder.getCommodityPrice();
        orderSon.setCommodityPrice(commodityPrice);
        int chooseNumber = orderSon.getChooseNumber();
        int storeId = commodityToOrder.getStoreId();

        OrderParent orderParent = null;
        orderSon.setCommodityPrice(commodityPrice);
        if (!storeId_orderParentMap.containsKey(storeId)){
            orderParent = new OrderParent(userId, storeId, orderTime);
            orderParent.setOrderSonList(new ArrayList<>());
            storeId_orderParentMap.put(storeId, orderParent);
        }else {
            orderParent = storeId_orderParentMap.get(storeId);
        }
        orderParent.setOrderSumNumber(orderParent.getOrderSumNumber() + chooseNumber);
        orderParent.setOrderSumPrice(orderParent.getOrderSumPrice() + chooseNumber * commodityPrice);
        orderParent.getOrderSonList().add(orderSon);
    }




    public Map<Integer, OrderParent> popResult(){

        Map<Integer, OrderParent> map = storeId_orderParentMap;
        storeId_orderParentMap = null;
        return map;
    }



}
