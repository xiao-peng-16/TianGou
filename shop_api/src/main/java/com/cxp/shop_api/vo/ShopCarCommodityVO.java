package com.cxp.shop_api.vo;

import com.cxp.shop_api.entity.ShopCar;
import lombok.Data;

@Data
public class ShopCarCommodityVO {

    ShopCar shopCar;

    int commodityId;            //商品id
    String commodityName;       //商品名
    String commodityDescribe;   //商品描述
    double commodityPrice;      //价格
    Boolean baoYou;             //是否包邮
    String commodityPhoto;  //商品图片

}
