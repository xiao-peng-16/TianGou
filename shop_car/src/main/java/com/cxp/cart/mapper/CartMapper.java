package com.cxp.cart.mapper;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.dto.CartPurchase;
import com.cxp.shop_api.entity.Cart;
import com.cxp.shop_api.result.ResultBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {

    //查用户购物车
    @Select("select * from cart where user_id=#{userId} order by cart_id desc")
    public List<Cart> listCartByUserId(@Param("userId") int userId);

    //查询用户购物车数量
    @Select("select count(*) from cart where user_id=#{userId} ")
    public int countCartByUserId(@Param("userId") int userId);

    //增加该条购物车的购买数量 根据 用户id 和 商品id
    @Update("update cart set purchase_quantity = purchase_quantity+#{purchaseQuantity} where commodity_id=#{commodityId} and user_id=#{userId}")
    public int updAddPurchaseQuantity(CartPurchase cartPurchase);

    //修改该条购物车的购买数量 根据 用户id 和 商品id
    @Update("update cart set purchase_quantity = #{purchaseQuantity} where commodity_id=#{commodityId} and user_id=#{userId}")
    public int updChangePurchaseQuantity(CartPurchase cartPurchase);

    //增加一条购物车
    public int insCart(CartPurchase cartPurchase);

    //删除一条购物车 根据这条购物车id  userId
    public int delCartByPurchaseDTO(@Param("userId") int userId,@Param("list") List<PurchaseDTO> purchaseDTOList);

    //删除一条购物车 根据这条购物车id, userId
    public int delCartByCommodityId(@Param("userId") int userId,@Param("list") List<Integer> cartIdList);


    //更新购物车全部选中状态
    @Update("update cart set selected = #{selected} where commodity_id = #{commodityId} and user_id = #{userId}")
    public int updSelectedByUserId(@Param("userId") Integer userId, @Param("commodityId") Integer commodityId, @Param("selected") Boolean selected);

    @Update("update cart set selected = #{selected} where user_id = #{userId}")
    public int updAllSelectedByUserId(Integer userId, Boolean selected);
}
