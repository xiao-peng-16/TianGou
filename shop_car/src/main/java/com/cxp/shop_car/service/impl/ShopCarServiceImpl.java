package com.cxp.shop_car.service.impl;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.dto.ShopCarPurchase;
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
    public ResultBean addShopCarByUserId(ShopCarPurchase shopCarPurchase) {
        //防止用户购买自己的商品
        if (commodityFeignClient.isCommodityStoreEqualUser(shopCarPurchase.getUserId(), shopCarPurchase.getCommodityId()))
            return STORE_EQUAL_USER_ERROR;

        try {
            shopCarMapper.insShop_Car(shopCarPurchase);
        }catch (Exception e){
            if(e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException)
                shopCarMapper.updAddPurchaseQuantity(shopCarPurchase); //如果购物车已经有该商品，就只增加数量
        }
        return successResult;
    }

    @Override
    public int countShopCarByUserId(int userId) {
        return shopCarMapper.countShopCarByUserId(userId);
    }


    @Override
    public List<ShopCarCommodityVO> listShopCarCommodityVOByUserId(Integer userId) {

        List<ShopCar> shopCarList = shopCarMapper.listShopCarByUserId(userId);
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
    public boolean updSelectedByUserId(Integer userId, Integer commodityId, Boolean selected) {
        if (null == selected)
            return false;
        return 0 != shopCarMapper.updSelectedByUserId(userId, commodityId, selected);
    }

    @Override
    public boolean updChangePurchaseQuantity(ShopCarPurchase shopCarPurchase) {
        if (1 > shopCarPurchase.getPurchaseQuantity())
            return false;
        return 0 != shopCarMapper.updChangePurchaseQuantity(shopCarPurchase);
    }


    @Override
    public ResultBean ShopCarSubmitOrderByUserId(int userId, List<PurchaseDTO> purchaseDTOList){
        ResultBean responseBean = orderFeignClient.submitMultipleOrderByUserId(userId, purchaseDTOList);
        shopCarMapper.delShopCarByPurchaseDTO(userId, purchaseDTOList);    //  删除购物车表信息
        return responseBean;
    }

    @Override
    public ResultBean delShopCarByCommodityId(int userId,List<Integer> commodityIdList){
        shopCarMapper.delShopCarByCommodityId(userId,commodityIdList);
        return successResult;
    }

    @Override
    public ResultBean shopCarToFavoriteByUserId(int userId, List<Integer> commodityIdList) {
         shopCarMapper.delShopCarByCommodityId(userId, commodityIdList);
         return favoriteFeignClient.addFavoriteByUserId(userId, commodityIdList);
    }


}
