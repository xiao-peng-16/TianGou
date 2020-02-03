package com.cxp.shop_commodity.mapper;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.CommodityToCart;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.Sort;
import com.cxp.shop_api.request.SearchRequest;
import com.cxp.shop_api.vo.CartCommodityVO;
import com.cxp.shop_api.vo.FavoriteCommodityVO;
import com.cxp.shop_api.vo.SearchCommodityVO;
import com.cxp.shop_api.vo.StoreCommodityVO;
import com.cxp.shop_commodity.pojo.CommodityPhotoVideo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommodityMapper {


    //查询商品种类列表
    @Select("select * from sort")
    public List<Sort> listSort();

    //添加商品
    public int insCommodity(Commodity commodity);
    //修改商品
    public int updCommodity(Commodity commodity);
    //查询商品图片 视频
    @Select("select commodity_photo, commodity_video from commodity where commodity_id = #{commodityId}")
    public CommodityPhotoVideo getCommodityPhotoVideo(@Param("commodityId") Integer commodityId);
    //修改商品上架状态
    @Update("update commodity set commodity_on_shelves = #{commodityOnShelves} where commodity_id = #{commodityId} and store_id = #{storeId}")
    public int updCommodityOnShelves(@Param("storeId") Integer storeId, @Param("commodityId") Integer commodityId, @Param("commodityOnShelves") Boolean commodityOnShelves);
    //删除商品 永久下架
    @Delete("delete from commodity where commodity_id = #{commodityId} and store_id = #{storeId}")
    public int delCommodity(Integer storeId, Integer commodityId);

//******搜索页   开始********
    //根据商品名字  查询搜索到的商品总数
    public int countSearchByCommodityName(SearchRequest searchPage_request);
    //根据商品名字  查询搜索页的商品信息
    public List<SearchCommodityVO> listSearchCommodityVOByCommodityName(SearchRequest searchPage_request);

    //根据商品种类的名字  查询搜索到的商品总数
    public Integer countSearchBySortName(SearchRequest searchPage_request);
    //根据商品种类的名字  查询搜索页的商品信息
    public List<SearchCommodityVO> listSearchCommodityVOBySortName(SearchRequest searchPage_request);

    //数据店铺名字拿到店铺id   现在根据店铺id查
    //根据店铺id查 查询搜索到的商品总数
    public Integer countSearchByStoreId(List<Integer> storeIdList);
    //根据店铺id查  查询搜索页的商品信息
    public List<SearchCommodityVO> listSearchCommodityVOByStoreId(@Param("list") List<Integer> storeIdList, @Param("searchPage_request") SearchRequest searchPage_request);

//******搜索页   结束********





    //查询一个商品完整数据 用于 商品頁 和 店铺修改商品
    @Select("select * from commodity where commodity_id = #{commodityId}")
    public Commodity getCommodityByCommodityId(@Param("commodityId") int commodityId);


    //搜索商品用于生成购物车
    public CommodityToCart getCommodityToCart(@Param("commodityId") int commodityId);
    //查询购物车商品信息
    @MapKey("commodityId")
    public Map<Integer, CartCommodityVO> mapCartCommodityVO(List<Integer> commodityIdList);

    //查询商品简单信息  用于 收藏夹
    @MapKey("commodityId")
    public Map<Integer, FavoriteCommodityVO> mapFavoriteCommodityVO(List<Integer> commodityIdList);
    //查询商品信息用于形成订单
    public  CommodityToOrder getCommodityToOrder(@Param("userId") Integer userId, @Param("commodityId") Integer commodityId);

    @MapKey("commodityId")
    public Map<Integer, CommodityToOrder> mapCommodityToOrder(@Param("userId") Integer userId,@Param("list")  List<Integer> commodityIdList);



    //用于加入购物车11 返回数量判断是否用户购买自己的商品
    @Select("select count(*) from commodity where  commodity_id = #{commodityId} and store_id = #{userId}")
    public int countCommodityStoreEqualUser(@Param("userId") int userId, @Param("commodityId") int commodityId);

    //浏览商品 人气+1
    @Update("update commodity set commodity_popularity=commodity_popularity +1 where commodity_id =#{commodityId}")
    public int updAddCPopularityByID(@Param("commodityId") int commodityId);
    //交易后更改商品表 商品数量
    @Update("update commodity set commodity_stock=commodity_stock - #{purchaseQuantity},commodity_sales=commodity_sales + #{purchaseQuantity} where commodity_id =#{commodityId}")
    public int updCommodityNumber(CommodityNumberChange commodityNumberChange);

    //查找 商家出售哪些商品
    public List<StoreCommodityVO> listStoreCommodityVOByStoreId(@Param("storeID") int storeID);
}
