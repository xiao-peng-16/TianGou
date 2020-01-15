package com.cxp.shop_favorite.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteMapper {

    //插入收藏夹商品 已经有了就不插入
    public int addFavorite(@Param("userId") int userId, @Param("commodityIdList") List<Integer> commodityIdList);


    //查询收藏夹商品
    @Select("select commodity_id from favorite where user_id=#{userId} order by favorite_id desc")
    public List<Integer> listFavoriteCommodityVO(@Param("userId") int userId);

    //删除收藏夹商品

    public int delFavorite(@Param("userId") int userId, @Param("commodityIdList") List<Integer> commodityIdList);

}
