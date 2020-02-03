package com.cxp.shop_api.entity;


import com.cxp.shop_api.vo.CartCommodityVO;
import lombok.Data;




@Data
public class Cart {

    int cartId;      //买家加入购物车 的编号
    int userId;         //用户id
    int commodityId;    //商品id
    int purchaseQuantity;   //数量
    boolean selected;       //选中

    String commodityName;       //商品名
    String commodityDescribe;   //商品描述
    double commodityOldPrice;      //价格
    String commodityPhoto;      //商品图片



    CartCommodityVO cartCommodityVO;
}


