package com.cxp.shop_favorite.controller;

import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_favorite.service.impl.FavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(allowCredentials = "true")
@RestController
public class FavoriteController {

    @Autowired
    FavoriteServiceImpl favoriteService;

    static final ResultBean successResult = ResultFactory.createSuccessResult();

    //添加收藏夹     购物车微服务 =》 购物车 移动到 收藏夹
    @PostMapping("/addFavoriteByUserId")
    public ResultBean addFavoriteByUserId(@RequestParam Integer userId, @RequestBody List<Integer> commodityIdList){
        favoriteService.addFavorite(userId, commodityIdList);
        return successResult;
    }

    //前端接口
    //返回收藏夹 商品信息
    @RequestMapping("/selFavoriteCommodityVOByUserId")
    public ResultBean selFavoriteCommodityVOByUserId(@RequestParam Integer userId){
        return ResultFactory.createSuccessResult(favoriteService.listFavoriteCommodityVO(userId));

    }



    //前端接口
    //删除收藏夹
    @PostMapping("/delFavoriteByUserId")
    public ResultBean delFavoriteByUserId(@RequestParam Integer userId, @RequestBody List<Integer> commodityIdList){
        favoriteService.delFavorite(userId, commodityIdList);
        return successResult;
    }
}
