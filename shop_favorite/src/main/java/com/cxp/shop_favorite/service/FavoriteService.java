package com.cxp.shop_favorite.service;




import com.cxp.shop_api.vo.FavoriteCommodityVO;

import java.util.List;


public interface FavoriteService {

    public void addFavorite(int userId, List<Integer> commodityIdList);

    public List<FavoriteCommodityVO> listFavoriteCommodityVO(int userId);

    public void delFavorite(int userId, List<Integer> commodityIdList);

}
