package com.cxp.shop_favorite.service.impl;

import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
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

    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean FAVORITE_ADD_ERROR = ResultFactory.createFailResult(ResultStatus.FAVORITE_ADD_ERROR);
    static final ResultBean FAVORITE_DEL_ERROR = ResultFactory.createFailResult(ResultStatus.FAVORITE_DEL_ERROR);

    @Override
    public ResultBean addFavorite(int userId,List<Integer> commodityIdList) {
        try {
            favoriteMapper.addFavorite(userId, commodityIdList);
            return successResult;
        }catch (Exception e){
            return FAVORITE_ADD_ERROR;
        }
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
    public ResultBean delFavorite(int userId,List<Integer> commodityIdList){
        try {
            favoriteMapper.delFavorite(userId, commodityIdList);
            return successResult;
        }catch (Exception e){
            return FAVORITE_DEL_ERROR;
        }
    }


}
