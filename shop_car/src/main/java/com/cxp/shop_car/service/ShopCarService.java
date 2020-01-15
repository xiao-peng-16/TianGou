package com.cxp.shop_car.service;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.dto.ShopCarPurchase;
import com.cxp.shop_api.entity.ShopCar;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.ShopCarCommodityVO;

import java.util.List;

public interface ShopCarService {

    //用户点击 加入购物车
    public ResultBean addShopCarByUserId(ShopCarPurchase shopCarPurchase);

    //购物车数量
    public int countShopCarByUserId(int userId);

    //购物车页面数据
    public List<ShopCarCommodityVO> listShopCarCommodityVOByUserId(Integer userId);


    //更新 购物车选中状态 根据 用户id 和 商品id
    public boolean updSelectedByUserId(Integer userId, Integer commodityId, Boolean selected);

    //更新 该条购物车的购买数量 根据 用户id 和 商品id
    public boolean updChangePurchaseQuantity(ShopCarPurchase shopCarPurchase);

    //购物车 购物
    public ResultBean ShopCarSubmitOrderByUserId(int userId, List<PurchaseDTO> purchaseDTOList);

    //删除一条购物车记录根据id
    public ResultBean delShopCarByCommodityId(int userId,List<Integer> commodityIdList);

    //删除一条购物车记录 并移入收藏夹
    public ResultBean shopCarToFavoriteByUserId(int userId, List<Integer> commodityIdList);

}
