package com.cxp.shop_car.service;

import com.cxp.shop_api.dto.AddShopCar;
import com.cxp.shop_api.dto.ShopCarToFavorite;
import com.cxp.shop_api.entity.ShopCar;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.ShopCarCommodityVO;

import java.util.List;

public interface ShopCarService {

    //用户点击 加入购物车
    public ResultBean addShopCarByUserId(Integer userId, AddShopCar addShopCar);

    //购物车数量
    public int selShopCarNumberByUserId(int userId);

    //购物车页面数据
    public List<ShopCarCommodityVO> listShopCarCommodityVOByUserId(Integer userId);

    //购物车 购物
    public ResultBean ShopCarSubmitOrderByUserId(int userId, List<ShopCar> shopCarList);

    //删除一条购物车记录根据id
    public ResultBean delShopCarByUserId(int userId,List<Integer> shopCarIdList);

    //删除一条购物车记录 并移入收藏夹
    public ResultBean shopCarToFavoriteByUserId(int userId, ShopCarToFavorite shopCarToFavorite);

}
