package com.cxp.shop_order.pojo;


import com.cxp.shop_api.dto.CommodityToOrder;
import lombok.Data;

@Data
public class AddOrderSon {

    long orderId;
    int commodityId;
    int purchaseQuantity;       //数量

    CommodityToOrder commodityToOrder;
}
