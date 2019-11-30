package com.cxp.shop_api.vo;

import com.cxp.shop_api.entity.OrderParent;
import lombok.Data;

import java.util.List;

@Data
public class OrderGeneraVO {

    int orderId;
    OrderParent orderParent;

    int chooseNumber;           //该orderParent 的 所有List<OrderSon>的 chooseNumber   之和
    double orderSumPrice;       //该orderParent 的 所有List<OrderSon>的 orderSumPrice  之和

    List<OrderCommodityVO> orderCommodityVOList;//商品简单信息

}
