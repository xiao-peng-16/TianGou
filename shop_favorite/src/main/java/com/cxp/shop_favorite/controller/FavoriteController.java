package com.cxp.shop_favorite.controller;

import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.vo.FavoriteCommodityVO;
import com.cxp.shop_favorite.service.impl.FavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(allowCredentials = "true")
@RestController
public class FavoriteController {

    @Autowired
    FavoriteServiceImpl favoriteService;


    //添加收藏夹     购物车微服务 =》 购物车 移动到 收藏夹
    @PostMapping("/addFavoriteByUserId")
    public ResultBean addFavoriteByUserId(Integer userId, @RequestBody List<Integer> commodityIdList){
        return favoriteService.addFavorite(userId, commodityIdList);
    }

    //前端接口
    //返回收藏夹 商品信息
    @RequestMapping("/listFavoriteCommodityVOByUserId")
    public List<FavoriteCommodityVO> listFavoriteCommodityVOByUserId(@RequestParam Integer userId){
        return favoriteService.listFavoriteCommodityVO(userId);

    }



    //前端接口
    //删除收藏夹
    @PostMapping("/delFavoriteByUserId")
    public ResultBean delFavoriteByUserId(@RequestParam Integer userId, @RequestBody List<Integer> commodityIdList){
        return favoriteService.delFavorite(userId, commodityIdList);
    }
}
