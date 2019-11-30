package com.cxp.shop_api.request;


import lombok.Data;

@Data
public class SearchRequest {
    public static final int PRICE_ASC = 1;          //价格升序
    public static final int PRICE_DESC = 2;         //价格降序
    public static final int SALES_VOLUME_DESC = 3;  //销量降序
    public static final int POPULARITY_DESC = 4;    //人气降序


    public static final int SEARCH_BY_COMMODITY = 0;    //根据商品名字搜索
    public static final int SEARCH_BY_STORE = 1;        //根据店铺名字搜索


    String  searchWord;      //根据  搜索词
    Integer searchManner;   //搜索方式    （根据商品名字搜索、根据店铺名字搜索）

    Integer pageNo;//  分页 页码
    Integer pageStepSize;//  分页 步长
    Integer pageStartLen;//  分页 第几行开始  计算得来

}
