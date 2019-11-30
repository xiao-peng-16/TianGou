package com.cxp.shop_api.entity;

import com.cxp.shop_api.vo.OrderCommodityVO;
import lombok.Data;
import lombok.experimental.Accessors;



@Data
@Accessors(chain = true)
public class OrderSon {

    int orderId;                //买家购买 交易形成的订单 的编号
    int storeId;                //卖家店铺id
    int commodityId;            //商品id
    int chooseNumber;           //数量
    double commodityPrice;      //'商品价格',
    double orderSumPrice;       //'该订单总金额',


    //非数据库字段
    String storeName;           //店铺名字
    //非数据库该表字段  商品简单信息 用于某一订单详细详细页面时
    OrderCommodityVO orderCommodityVO;
}
