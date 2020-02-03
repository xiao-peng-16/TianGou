package com.cxp.cart.service.impl;

import com.cxp.shop_api.dto.CommodityToCart;
import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.dto.CartPurchase;
import com.cxp.shop_api.entity.Cart;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.CartCommodityVO;
import com.cxp.cart.mapper.CartMapper;
import com.cxp.cart.service.FeignClient.CommodityFeignClient;
import com.cxp.cart.service.FeignClient.FavoriteFeignClient;
import com.cxp.cart.service.FeignClient.OrderFeignClient;
import com.cxp.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    CommodityFeignClient commodityFeignClient;
    @Autowired
    FavoriteFeignClient favoriteFeignClient;
    @Autowired
    OrderFeignClient orderFeignClient;


    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean STORE_EQUAL_USER_ERROR = ResultFactory.createFailResult(ResultStatus.STORE_EQUAL_USER_ERROR);
    static final ResultBean ORDER_NOT_FIND =ResultFactory.createFailResult(ResultStatus.ORDER_NOT_FIND);
    @Override
    public ResultBean addCartByUserId(CartPurchase cartPurchase) {
        //防止用户购买自己的商品
        if (commodityFeignClient.isCommodityStoreEqualUser(cartPurchase.getUserId(), cartPurchase.getCommodityId()))
            return STORE_EQUAL_USER_ERROR;

        CommodityToCart commodityToCart = commodityFeignClient.getCommodityToCart(cartPurchase.getCommodityId());
        if (null == commodityToCart)
            return ORDER_NOT_FIND;
        cartPurchase.setCommodityToCart(commodityToCart);

        try {
            cartMapper.insCart(cartPurchase);
        }catch (Exception e){
            if(e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException)
                cartMapper.updAddPurchaseQuantity(cartPurchase); //如果购物车已经有该商品，就只增加数量
        }
        return successResult;
    }

    @Override
    public int countCartByUserId(int userId) {
        return cartMapper.countCartByUserId(userId);
    }


    @Override
    public List<Cart> listCartCommodityVOByUserId(Integer userId) {

        List<Cart> cartList = cartMapper.listCartByUserId(userId);
        if (0 == cartList.size()) return null;
        List<Integer> commodityIdList = cartList.stream().map(e -> e.getCommodityId()).collect(Collectors.toList());
        Map<Integer, CartCommodityVO> cartCommodityVOMap = commodityFeignClient.mapCartCommodityVO(commodityIdList);

        cartList.stream().forEach(cart -> {
            cart.setCartCommodityVO(cartCommodityVOMap.get(cart.getCommodityId()));
        });
        return cartList;
    }

    @Override
    public boolean updSelectedByUserId(Integer userId, Integer commodityId, Boolean selected) {
        if (null == selected)
            return false;
        return 0 != cartMapper.updSelectedByUserId(userId, commodityId, selected);
    }

    @Override
    public boolean updAllSelectedByUserId(Integer userId, Boolean selected) {
        if (null == selected)
            return false;
        return 0 != cartMapper.updAllSelectedByUserId(userId, selected);
    }

    @Override
    public boolean updChangePurchaseQuantity(CartPurchase cartPurchase) {
        if (1 > cartPurchase.getPurchaseQuantity())
            return false;
        return 0 != cartMapper.updChangePurchaseQuantity(cartPurchase);
    }


    @Override
    public ResultBean CartSubmitOrderByUserId(int userId, List<PurchaseDTO> purchaseDTOList){
        ResultBean responseBean = orderFeignClient.submitMultipleOrderByUserId(userId, purchaseDTOList);
        if (responseBean.isSuccess())
            cartMapper.delCartByPurchaseDTO(userId, purchaseDTOList);    //  删除购物车表信息
        return responseBean;
    }

    @Override
    public ResultBean delCartByCommodityId(int userId,List<Integer> commodityIdList){
        cartMapper.delCartByCommodityId(userId,commodityIdList);
        return successResult;
    }

    @Override
    public ResultBean cartToFavoriteByUserId(int userId, List<Integer> commodityIdList) {
         cartMapper.delCartByCommodityId(userId, commodityIdList);
         return favoriteFeignClient.addFavoriteByUserId(userId, commodityIdList);
    }


}
