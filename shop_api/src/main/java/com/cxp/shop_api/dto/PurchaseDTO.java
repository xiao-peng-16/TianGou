package com.cxp.shop_api.dto;



import lombok.Data;

@Data
public class PurchaseDTO {
    int commodityId;        //商品id
    int purchaseQuantity;   //数量
}
