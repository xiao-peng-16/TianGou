package com.cxp.shop_api.dto;

import lombok.Data;

@Data
public class CommodityToOrder{

    int commodityId;
    int storeId;                //店铺id
    double commodityPrice;      //价格
    int commodityStock;         //库存

}
