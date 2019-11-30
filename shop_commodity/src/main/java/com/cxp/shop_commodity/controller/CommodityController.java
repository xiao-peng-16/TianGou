package com.cxp.shop_commodity.controller;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.request.SearchRequest;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.vo.FavoriteCommodityVO;
import com.cxp.shop_api.vo.OrderCommodityVO;
import com.cxp.shop_api.vo.SearchVO;
import com.cxp.shop_api.vo.ShopCarCommodityVO;
import com.cxp.shop_commodity.service.impl.CommodityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/*

    商品
 */
@CrossOrigin(allowCredentials = "true")
@RestController
public class CommodityController {

    @Autowired
    CommodityServiceImpl commodityService;

    //收藏夹 微服务
    @PostMapping("/mapFavoriteCommodityVO")
    public Map<Integer, FavoriteCommodityVO> mapFavoriteCommodityVO(@RequestBody List<Integer> commodityIdList){
        return commodityService.mapFavoriteCommodityVO(commodityIdList);
    }

    //购物车 微服务
    @RequestMapping("/mapShopCarCommodityVO")
    public Map<Integer, ShopCarCommodityVO> mapShopCarCommodityVO(@RequestBody  List<Integer> commodityIdList){
        return  commodityService.mapShopCarCommodityVO(commodityIdList);
    }

    //订单管理 微服务
    @PostMapping("/mapOrderCommodityVO")
    public Map<Integer, OrderCommodityVO> mapOrderCommodityVO(@RequestBody List<Integer> commodityIdList){
        return commodityService.mapOrderCommodityVO(commodityIdList);
    }

    //前端接口
    //搜索页
    @RequestMapping("/selSearchVO")
    public SearchVO selSearchVO(SearchRequest searchPage_request) {
        if (null == searchPage_request.getSearchWord()) return null;
        return commodityService.selSearchVO(searchPage_request);
    }

    //前端接口
    //商品页
    @RequestMapping("/selCommodityByCommodityID")
    public Commodity selCommodityByCommodityID(Integer commodityId){
        return  commodityService.selCommodityByCommodityID(commodityId);
    }

    //前端接口
    //卖家中心  销售的商品
    @RequestMapping("/selStoreCommodityVOByUserId")
    public ResultBean selStoreCommodityVOByUserId(Integer userId){
        return  ResultFactory.createSuccessResult(commodityService.selStoreCommodityByUserId(userId));
    }


//***************************上面的接口 用于 页面展示商品信息***************************************************

    //判断买家是否把自己的商品添加到购物车
    @RequestMapping("isCommodityStoreEqualUser")
    public boolean isCommodityStoreEqualUser(int userId, int commodityId) {
        return commodityService.isCommodityStoreEqualUser(userId, commodityId);
    }

    //前端接口
    //提交订单： 接收商品id 数量 返回订单orderId
    @PostMapping("/submitOrderByUserId")
    public ResultBean submitOrder(@RequestParam Integer userId, @RequestBody LinkedList<OrderSon> orderSonList){
        return commodityService.submitOrder(userId, orderSonList);
    }

    //修改商品 库存 销量
    @PostMapping("/updCommodityNumber")
    public ResultBean updCommodityNumber(@RequestBody List<CommodityNumberChange> commodityNumberChangeList){
        return commodityService.updCommodityNumber(commodityNumberChangeList);
    }


}
