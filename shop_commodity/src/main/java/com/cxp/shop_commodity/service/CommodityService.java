package com.cxp.shop_commodity.service;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.Sort;
import com.cxp.shop_api.request.SearchRequest;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.*;

import java.util.List;
import java.util.Map;

public interface CommodityService {

    //查询商品种类列表
    public List<Sort> listSort();

    //添加商品
    public ResultBean addCommodity(Commodity commodity);

    //修改商品
    public ResultBean updCommodity(Commodity commodity);


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

    //查询商品 用于 店铺修改商品
    public Commodity selCommodityByCommodityId(int commodityId);

    //返回  店铺所销售的信息
    //判断是否 用户购买自己的视频
    public boolean isCommodityStoreEqualUser(int userId, int commodityId);

    //用于订单微服务 提交订单  需要的店铺id 单价  库存
    public  Map<Integer, CommodityToOrder> mapCommodityToOrder(List<Integer> commodityIdList);
    //修改商品 库存 销量
    public ResultBean updCommodityNumber(List<CommodityNumberChange> commodityNumberChangeList);

}
