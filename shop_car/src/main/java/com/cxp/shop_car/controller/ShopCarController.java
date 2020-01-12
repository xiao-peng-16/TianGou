package com.cxp.shop_car.controller;

import com.cxp.shop_api.dto.AddShopCar;
import com.cxp.shop_api.dto.ShopCarToFavorite;
import com.cxp.shop_api.entity.ShopCar;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_car.service.impl.ShopCarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*

    购物车
 */
@CrossOrigin(allowCredentials = "true")
@RestController
public class ShopCarController {

    @Autowired
    ShopCarServiceImpl shopCarService;


    //前端接口
    // 添加购物车
    @RequestMapping("/addShopCarByUserId")
    public ResultBean addShopCarByUserId(AddShopCar addShopCar){
        return shopCarService.addShopCarByUserId(addShopCar);
    }

    //查看购物车数量
    @RequestMapping("/selShopCarCountByUserId")
    public int selShopCarCountByUserId(Integer userId) {
        return shopCarService.selShopCarCountByUserId(userId);
    }

    //前端接口
    //查看购物车商品
    @RequestMapping("/listShopCarCommodityVOByUserId")
    public ResultBean listShopCarCommodityVOByUserId(Integer userId) {
        return ResultFactory.createSuccessResult(shopCarService.listShopCarCommodityVOByUserId(userId));
    }

    //前端接口
    //购物车页面购物
    @PostMapping("/ShopCarSubmitOrderByUserId")
    public ResultBean ShopCarSubmitOrderByUserId(@RequestParam Integer userId, @RequestBody List<ShopCar> shopCarList){
        return shopCarService.ShopCarSubmitOrderByUserId(userId, shopCarList);
    }

    //前端接口
    //购物车删除商品
    @PostMapping("/delShopCarByUserId")
    public ResultBean delShopCarByUserId(@RequestParam Integer userId,@RequestBody List<Integer> shopCarIdList){
        return shopCarService.delShopCarByUserId(userId,shopCarIdList);
    }


    //前端接口
    //购物车移入收藏夹
    @PostMapping("/shopCarToFavoriteByUserId")
    public ResultBean shopCarToFavoriteByUserId(@RequestParam Integer userId, @RequestBody ShopCarToFavorite shopCarToFavorite){
        return  shopCarService.shopCarToFavoriteByUserId(userId, shopCarToFavorite);
    }
}
