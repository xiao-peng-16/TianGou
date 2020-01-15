package com.cxp.shop_car.controller;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.dto.ShopCarPurchase;
import com.cxp.shop_api.entity.ShopCar;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.ShopCarCommodityVO;
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
    public ResultBean addShopCarByUserId(ShopCarPurchase shopCarPurchase){
        return shopCarService.addShopCarByUserId(shopCarPurchase);
    }

    //查看购物车数量
    @RequestMapping("/countShopCarByUserId")
    public int countShopCarByUserId(Integer userId) {
        return shopCarService.countShopCarByUserId(userId);
    }

    //前端接口
    //查看购物车商品
    @RequestMapping("/listShopCarCommodityVOByUserId")
    public List<ShopCarCommodityVO> listShopCarCommodityVOByUserId(Integer userId) {
        return shopCarService.listShopCarCommodityVOByUserId(userId);
    }


    //前端接口
    //更新购物车选中状态
    @RequestMapping("/updSelectedByUserId")
    public boolean updSelectedByUserId(Integer userId, Integer commodityId, Boolean selected){
        return shopCarService.updSelectedByUserId(userId, commodityId, selected);
    }

    //前端接口
    //更新 该条购物车的购买数量 根据 用户id 和 商品id
    @RequestMapping("/updChangePurchaseQuantityByUserId")
    public boolean updChangePurchaseQuantityByUserId(ShopCarPurchase shopCarPurchase){
        return shopCarService.updChangePurchaseQuantity(shopCarPurchase);
    }



    //前端接口
    //购物车页面购物
    @PostMapping("/ShopCarSubmitOrderByUserId")
    public ResultBean ShopCarSubmitOrderByUserId(Integer userId, @RequestBody List<PurchaseDTO> purchaseDTOList){
        return shopCarService.ShopCarSubmitOrderByUserId(userId, purchaseDTOList);
    }

    //前端接口
    //购物车删除商品
    @PostMapping("/delShopCarByUserId")
    public ResultBean delShopCarByUserId(Integer userId,@RequestBody List<Integer> commodityIdList){
        return shopCarService.delShopCarByCommodityId(userId,commodityIdList);
    }


    //前端接口
    //购物车移入收藏夹
    @PostMapping("/shopCarToFavoriteByUserId")
    public ResultBean shopCarToFavoriteByUserId(Integer userId, @RequestBody List<Integer> commodityIdList){
        return  shopCarService.shopCarToFavoriteByUserId(userId, commodityIdList);
    }

}
