package com.cxp.cart.service;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.dto.CartPurchase;
import com.cxp.shop_api.entity.Cart;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.CartCommodityVO;

import java.util.List;

public interface CartService {

    //用户点击 加入购物车
    public ResultBean addCartByUserId(CartPurchase cartPurchase);

    //购物车数量
    public int countCartByUserId(int userId);

    //购物车页面数据
    public List<Cart> listCartCommodityVOByUserId(Integer userId);


    //更新 购物车选中状态 根据 用户id 和 商品id
    public boolean updSelectedByUserId(Integer userId, Integer commodityId, Boolean selected);
    public boolean updAllSelectedByUserId(Integer userId, Boolean selected);

    //更新 该条购物车的购买数量 根据 用户id 和 商品id
    public boolean updChangePurchaseQuantity(CartPurchase cartPurchase);

    //购物车 购物
    public ResultBean CartSubmitOrderByUserId(int userId, List<PurchaseDTO> purchaseDTOList);

    //删除一条购物车记录根据id
    public ResultBean delCartByCommodityId(int userId,List<Integer> commodityIdList);

    //删除一条购物车记录 并移入收藏夹
    public ResultBean cartToFavoriteByUserId(int userId, List<Integer> commodityIdList);

}
