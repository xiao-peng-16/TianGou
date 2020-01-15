package com.cxp.shop_order.utils;

import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.entity.AddMultipleOrderParent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubmitOrderUtils  {


    static final SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Map<Integer, AddMultipleOrderParent> storeId_addMultipleOrderParentMap = new HashMap<>();
    int userId;
    String orderTime = dateFormat.format(new Date());


    public SubmitOrderUtils(int userId) {
        this.userId = userId;
    }




    public void work(OrderSon orderSon, CommodityToOrder commodityToOrder){

        double commodityPrice = commodityToOrder.getCommodityPrice();
        orderSon.setCommodityPrice(commodityPrice);
        int purchaseQuantity = orderSon.getPurchaseQuantity();
        int storeId = commodityToOrder.getStoreId();

        AddMultipleOrderParent addMultipleOrderParent = null;
        orderSon.setCommodityPrice(commodityPrice);
        if (!storeId_addMultipleOrderParentMap.containsKey(storeId)){
            addMultipleOrderParent = new AddMultipleOrderParent(userId, storeId, orderTime);
            addMultipleOrderParent.setOrderSonList(new ArrayList<>());
            storeId_addMultipleOrderParentMap.put(storeId, addMultipleOrderParent);
        }else {
            addMultipleOrderParent = storeId_addMultipleOrderParentMap.get(storeId);
        }
        addMultipleOrderParent.setOrderTotalQuantity(addMultipleOrderParent.getOrderTotalQuantity() + purchaseQuantity);
        addMultipleOrderParent.setOrderTotalPrice(addMultipleOrderParent.getOrderTotalPrice() + purchaseQuantity * commodityPrice);
        addMultipleOrderParent.getOrderSonList().add(orderSon);
    }




    public Map<Integer, AddMultipleOrderParent> popResult(){

        Map<Integer, AddMultipleOrderParent> map = storeId_addMultipleOrderParentMap;
        storeId_addMultipleOrderParentMap = null;
        return map;
    }



}
