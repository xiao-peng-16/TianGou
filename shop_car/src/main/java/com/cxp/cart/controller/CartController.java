package com.cxp.cart.controller;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.dto.CartPurchase;
import com.cxp.shop_api.entity.Cart;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.CartCommodityVO;
import com.cxp.cart.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*

    购物车
 */
@CrossOrigin(allowCredentials = "true")
@RestController
public class CartController {

    @Autowired
    CartServiceImpl cartService;


    //前端接口
    // 添加购物车
    @RequestMapping("/addCartByUserId")
    public ResultBean addCartByUserId(CartPurchase cartPurchase){
        return cartService.addCartByUserId(cartPurchase);
    }

    //查看购物车数量
    @RequestMapping("/countCartByUserId")
    public int countCartByUserId(Integer userId) {
        return cartService.countCartByUserId(userId);
    }

    //前端接口
    //查看购物车商品
    @RequestMapping("/listCartCommodityVOByUserId")
    public List<Cart> listCartCommodityVOByUserId(Integer userId) {
        return cartService.listCartCommodityVOByUserId(userId);
    }


    //前端接口
    //更新购物车选中状态
    @RequestMapping("/updSelectedByUserId")
    public boolean updSelectedByUserId(Integer userId, Integer commodityId, Boolean selected){
        return cartService.updSelectedByUserId(userId, commodityId, selected);
    }

    //更新全部购物车选中状态
    @RequestMapping("/updAllSelectedByUserId")
    public boolean updAllSelectedByUserId(Integer userId, Boolean selected){
        return cartService.updAllSelectedByUserId(userId, selected);
    }

    //前端接口
    //更新 该条购物车的购买数量 根据 用户id 和 商品id
    @RequestMapping("/updChangePurchaseQuantityByUserId")
    public boolean updChangePurchaseQuantityByUserId(CartPurchase cartPurchase){
        return cartService.updChangePurchaseQuantity(cartPurchase);
    }



    //前端接口
    //购物车页面购物
    @PostMapping("/CartSubmitOrderByUserId")
    public ResultBean CartSubmitOrderByUserId(Integer userId, @RequestBody List<PurchaseDTO> purchaseDTOList){
        return cartService.CartSubmitOrderByUserId(userId, purchaseDTOList);
    }

    //前端接口
    //购物车删除商品
    @PostMapping("/delCartByUserId")
    public ResultBean delCartByUserId(Integer userId,@RequestBody List<Integer> commodityIdList){
        return cartService.delCartByCommodityId(userId,commodityIdList);
    }


    //前端接口
    //购物车移入收藏夹
    @PostMapping("/cartToFavoriteByUserId")
    public ResultBean cartToFavoriteByUserId(Integer userId, @RequestBody List<Integer> commodityIdList){
        return  cartService.cartToFavoriteByUserId(userId, commodityIdList);
    }

}
