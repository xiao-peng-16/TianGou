package com.cxp.shop_commodity.service;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.request.SearchRequest;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface CommodityService {

    //根据商品id返回商品简单信息给 订单管理微服务 调用
    public Map<Integer, OrderCommodityVO> mapOrderCommodityVO(List<Integer> commodityIdList);
    //根据商品id返回商品简单信息给 收藏夹微服务 调用
    public Map<Integer, FavoriteCommodityVO> mapFavoriteCommodityVO(List<Integer> commodityIdList);


    //根据商品id返回 一个商品数据
    public Commodity selCommodityByCommodityID(int commodityId);
    //返回搜索页 商品信息
    public SearchVO selSearchVO(SearchRequest searchPage_request);
    //返回购物车 商品信息
    public Map<Integer, ShopCarCommodityVO> mapShopCarCommodityVO(List<Integer> commodityIdList);
    //返回  店铺所销售的商品信息
    public List<StoreCommodityVO> selStoreCommodityByUserId(int userId);
    //返回  店铺所销售的信息
    //判断是否 用户购买自己的视频
    public boolean isCommodityStoreEqualUser(int userId, int commodityId);
    //用户购买那些商品几件
    public ResultBean submitOrder(Integer userId, LinkedList<OrderSon> orderSonList);
    //修改商品 库存 销量
    public ResultBean updCommodityNumber(List<CommodityNumberChange> commodityNumberChangeList);

}
