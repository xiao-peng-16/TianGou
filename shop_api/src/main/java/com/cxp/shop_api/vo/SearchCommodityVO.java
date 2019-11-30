package com.cxp.shop_api.vo;

import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.entity.Sort;
import lombok.Data;

@Data
public class SearchCommodityVO {

    int commodityId;            //商品id
    String commodityName;       //商品名
    int storeId;
    StoreToCommodity storeToCommodity;

    Sort sort;                  //商品所属种类
    String commodityDescribe;   //商品描述
    Boolean baoYou;             //是否包邮
    String commodityPhotoname;  //商品图片
    double commodityPrice;      //价格
    int commoditySales;         //销量

}
