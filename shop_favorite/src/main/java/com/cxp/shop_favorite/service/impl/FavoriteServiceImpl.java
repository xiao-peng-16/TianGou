package com.cxp.shop_favorite.service.impl;

import com.cxp.shop_api.vo.FavoriteCommodityVO;
import com.cxp.shop_favorite.mapper.FavoriteMapper;
import com.cxp.shop_favorite.service.FavoriteService;
import com.cxp.shop_favorite.service.FeignClient.CommodityFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteMapper favoriteMapper;
    @Autowired
    CommodityFeignClient commodityFeignClient;

    @Override
    public void addFavorite(int userId,List<Integer> commodityIdList) {
        favoriteMapper.addFavorite(userId, commodityIdList);
    }

    @Override
    public List<FavoriteCommodityVO> listFavoriteCommodityVO(int UserId) {
        List<Integer> commodityIdList =  favoriteMapper.listFavoriteCommodityVO(UserId);
        if (0 == commodityIdList.size())
            return null;
        Map<Integer, FavoriteCommodityVO> favoriteCommodityVOMap = commodityFeignClient.mapFavoriteCommodityVO(commodityIdList);
        List<FavoriteCommodityVO> FavoriteCommodityVOList = new ArrayList<>();
        //按commodityIdList排序  ，commodityIdList从数据库拿是时间倒序
        for (Integer commodityId : commodityIdList){
            FavoriteCommodityVO item = favoriteCommodityVOMap.get(commodityId);
            if (item !=null)
                FavoriteCommodityVOList.add(item);
        }
        return FavoriteCommodityVOList;
    }

    @Override
    public void delFavorite(int userId,List<Integer> commodityIdList){
            favoriteMapper.delFavorite(userId, commodityIdList);
    }


}
