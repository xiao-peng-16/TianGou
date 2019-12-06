package com.cxp.shop_car.service.impl;

import com.cxp.shop_api.dto.AddShopCar;
import com.cxp.shop_api.dto.ShopCarToFavorite;
import com.cxp.shop_api.entity.ShopCar;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.vo.ShopCarCommodityVO;
import com.cxp.shop_car.mapper.ShopCarMapper;
import com.cxp.shop_car.service.FeignClient.CommodityFeignClient;
import com.cxp.shop_car.service.FeignClient.FavoriteFeignClient;
import com.cxp.shop_car.service.FeignClient.OrderFeignClient;
import com.cxp.shop_car.service.ShopCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShopCarServiceImpl implements ShopCarService {

    @Autowired
    ShopCarMapper shopCarMapper;
    @Autowired
    CommodityFeignClient commodityFeignClient;
    @Autowired
    FavoriteFeignClient favoriteFeignClient;
    @Autowired
    OrderFeignClient orderFeignClient;


    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean STORE_EQUAL_USER_ERROR = ResultFactory.createFailResult(ResultStatus.STORE_EQUAL_USER_ERROR);

    @Override
    public ResultBean addShopCarByUserId(Integer userId, AddShopCar addShopCar) {
        //防止用户购买自己的商品
        if (commodityFeignClient.isCommodityStoreEqualUser(userId, addShopCar.getCommodityId()))
            return STORE_EQUAL_USER_ERROR;
        addShopCar.setUserId(userId);
        if (0 == shopCarMapper.updShopCarNumberByCommodityId_UserId(addShopCar))//如果购物车已经有该商品，就只增加数量
            shopCarMapper.insShop_Car(addShopCar);    //购物车没该商品 则添加
        return successResult;
    }

    @Override
    public ResultBean selShopCarNumberByUserId(int userId) {
        return ResultFactory.createSuccessResult(shopCarMapper.selShopCarNumberByUserId(userId));
    }


    @Override
    public List<ShopCarCommodityVO> listShopCarCommodityVOByUserId(Integer userId) {

        List<ShopCar> shopCarList = shopCarMapper.selShopCarByUserId(userId);
        if (0 == shopCarList.size()) return null;
        List<Integer> commodityIdList = shopCarList.stream().map(e -> e.getCommodityId()).collect(Collectors.toList());
        Map<Integer, ShopCarCommodityVO> shopCarCommodityVOMap = commodityFeignClient.mapShopCarCommodityVO(commodityIdList);

        ShopCarCommodityVO shopCarCommodityVO =null;
        List<ShopCarCommodityVO> shopCarCommodityVOList = new ArrayList<>();
        for (ShopCar shopCar : shopCarList){
            shopCarCommodityVO = shopCarCommodityVOMap.get(shopCar.getCommodityId());
            if (shopCarCommodityVO != null){
                shopCarCommodityVO.setShopCar(shopCar);
                shopCarCommodityVOList.add(shopCarCommodityVO);
            }
        }
        return shopCarCommodityVOList;
    }



    @Override
    public ResultBean ShopCarSubmitOrderByUserId(int userId, List<ShopCar> shopCarList){
        ResultBean responseBean = orderFeignClient.submitOrderByUserId(userId, shopCarList);
        if (responseBean.isSuccess())
            shopCarMapper.delShopCarByShopCar(userId, shopCarList);    //  删除购物车表信息
        return responseBean;
    }

    @Override
    public ResultBean delShopCarByUserId(int userId,List<Integer> shopCarIdList){
        shopCarMapper.delShopCarByShopCarId(userId,shopCarIdList);
        return successResult;
    }

    @Override
    public ResultBean shopCarToFavoriteByUserId(int userId, ShopCarToFavorite shopCarToFavorite) {
         shopCarMapper.delShopCarByShopCarId(userId,shopCarToFavorite.getShopCarIdList());
         return favoriteFeignClient.addFavoriteByUserId(userId, shopCarToFavorite.getCommodityIdList());
    }


}
