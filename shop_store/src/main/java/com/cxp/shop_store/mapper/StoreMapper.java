package com.cxp.shop_store.mapper;

import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.entity.Store;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StoreMapper {

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息
    @MapKey("storeId")
    public Map<Integer,StoreToCommodity> mapStoreToCommodityByStoreId(List<Integer> storeIdList);

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺名字 模糊查询
    @MapKey("storeId")
    public Map<Integer,StoreToCommodity> selStoreToCommodityMapByStoreName(String searchWord);

    //返回店铺完整信息 根据storeId  用于商品页 和 店铺页
    @Select("select * from store where store_id = #{storeId}")
    public Store selStoreByStoreId(@Param("storeId") int storeId);
}
