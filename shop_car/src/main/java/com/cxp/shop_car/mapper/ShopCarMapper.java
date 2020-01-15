package com.cxp.shop_car.mapper;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.dto.ShopCarPurchase;
import com.cxp.shop_api.entity.ShopCar;
import com.cxp.shop_api.result.ResultBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCarMapper {

    //查用户购物车
    @Select("select * from shop_car where user_id=#{userId} order by shop_car_id desc")
    public List<ShopCar> listShopCarByUserId(@Param("userId") int userId);

    //查询用户购物车数量
    @Select("select count(*) from shop_car where user_id=#{userId} ")
    public int countShopCarByUserId(@Param("userId") int userId);

    //增加该条购物车的购买数量 根据 用户id 和 商品id
    @Update("update shop_car set purchase_quantity = purchase_quantity+#{purchaseQuantity} where user_id=#{userId} and commodity_id=#{commodityId}")
    public int updAddPurchaseQuantity(ShopCarPurchase shopCarPurchase);

    //修改该条购物车的购买数量 根据 用户id 和 商品id
    @Update("update shop_car set purchase_quantity = #{purchaseQuantity} where user_id=#{userId} and commodity_id=#{commodityId}")
    public int updChangePurchaseQuantity(ShopCarPurchase shopCarPurchase);

    //增加一条购物车
    @Insert("insert shop_car(user_id,commodity_id,purchase_quantity) values(#{userId},#{commodityId},#{purchaseQuantity})")
    public int insShop_Car(ShopCarPurchase shopCarPurchase);

    //删除一条购物车 根据这条购物车id  userId
    public int delShopCarByPurchaseDTO(@Param("userId") int userId,@Param("list") List<PurchaseDTO> purchaseDTOList);

    //删除一条购物车 根据这条购物车id, userId
    public int delShopCarByCommodityId(@Param("userId") int userId,@Param("list") List<Integer> shopCarIdList);


    //更新购物车全部选中状态
    public int updSelectedByUserId(@Param("userId") Integer userId, @Param("commodityId") Integer commodityId, @Param("selected") Boolean selected);
}
