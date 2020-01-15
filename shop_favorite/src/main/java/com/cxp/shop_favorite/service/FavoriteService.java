package com.cxp.shop_favorite.service;




import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.FavoriteCommodityVO;

import java.util.List;


public interface FavoriteService {

    public ResultBean addFavorite(int userId, List<Integer> commodityIdList);

    public List<FavoriteCommodityVO> listFavoriteCommodityVO(int userId);

    public ResultBean delFavorite(int userId, List<Integer> commodityIdList);

}
