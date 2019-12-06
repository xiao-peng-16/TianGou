package com.cxp.shop_commodity.mapper;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.request.SearchRequest;
import com.cxp.shop_api.vo.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommodityMapper {

//******搜索页   开始********
    //根据商品名字  查询搜索到的商品总数
    public Integer selSearchCountByCommodityName(SearchRequest searchPage_request);
    //根据商品名字  查询搜索页的商品信息
    public List<SearchCommodityVO> selSearchCommodityVOByCommodityName(SearchRequest searchPage_request);

    //根据商品种类的名字  查询搜索到的商品总数
    public Integer selSearchCountBySortName(SearchRequest searchPage_request);
    //根据商品种类的名字  查询搜索页的商品信息
    public List<SearchCommodityVO> selSearchCommodityVOBySortName(SearchRequest searchPage_request);

    //数据店铺名字拿到店铺id   现在根据店铺id查
    //根据店铺id查 查询搜索到的商品总数
    public Integer selSearchCountByStoreId(List<Integer> storeIdList);
    //根据店铺id查  查询搜索页的商品信息
    public List<SearchCommodityVO> selSearchCommodityVOByStoreId(@Param("list")List<Integer> storeIdList,@Param("searchPage_request") SearchRequest searchPage_request);

//******搜索页   结束********




    //商品頁 根据商品id返回 一个商品完整数据
    @Select("select * from commodity where commodity_id = #{commodityId}")
    public Commodity selCommodityByCommodityID(@Param("commodityId") int commodityId);

    //根据商品id返回 商品简单信息  图片、名字
    public List<OrderCommodityVO> SelCommoditySimplePage(List<Integer> commodityIdList);


    //查询购物车商品信息
    @MapKey("commodityId")
    public Map<Integer, ShopCarCommodityVO> mapShopCarCommodityVO(List<Integer> commodityIdList);
    //查询商品简单信息  用于  订单
    @MapKey("commodityId")
    public Map<Integer, OrderCommodityVO> mapOrderCommodityVO(List<Integer> commodityIdList);
    //查询商品简单信息  用于 收藏夹
    @MapKey("commodityId")
    public Map<Integer, FavoriteCommodityVO> mapFavoriteCommodityVO(List<Integer> commodityIdList);
    //查询商品信息用于形成订单
    @MapKey("commodityId")
    public Map<Integer, CommodityToOrder> mapCommodityToOrder(List<Integer> commodityIdList);

    //用于加入购物车 返回数量判断是否用户购买自己的视频
    @Select("select count(*) from commodity where store_id = #{userId} and commodity_id = #{commodityId}  ")
    public int selcommodityStoreEqualUser(@Param("userId") int userId, @Param("commodityId") int commodityId);

    //浏览商品 人气+1
    @Update("update commodity set commodity_popularity=commodity_popularity +1 where commodity_id =#{commodityId}")
    public int updAddCPopularityByID(@Param("commodityId") int commodityId);
    //交易后更改商品表 商品数量
    @Update("update commodity set commodity_stock=commodity_stock - #{chooseNumber},commodity_sales=commodity_sales + #{chooseNumber} where commodity_id =#{commodityId}")
    public int updCommodityNumber(CommodityNumberChange commodityNumberChange);

    //查找 商家出售哪些商品
    public List<StoreCommodityVO> selStoreCommodityByUserId(@Param("storeID") int storeID);
}
