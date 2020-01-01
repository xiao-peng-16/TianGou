package com.cxp.shop_commodity.controller;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.CommodityToOrder;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.Sort;
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

    //前端接口
    //查询商品1种类列表
    @RequestMapping("/listSort")
    public List<Sort> listSort(){
        return commodityService.listSort();
    }

    //前端接口
    //添加商品
    @RequestMapping("/addCommodityByStoreId")
    public ResultBean addCommodityByStoreId(Integer storeId,@RequestBody Commodity commodity){
        commodity.setStoreId(storeId);
        return commodityService.addCommodity(commodity);
    }

    //前端接口
    //修改商品上架状态
    @RequestMapping("/updCommodityOnShelvesByStoreId")
    public ResultBean updCommodityOnShelvesByStoreId(Integer storeId, Integer commodityId, Boolean commodityOnShelves){
        return commodityService.updCommodityOnShelves(storeId, commodityId, commodityOnShelves);
    }

    //前端接口
    //修改商品
    @RequestMapping("/updCommodityByStoreId")
    public ResultBean updCommodityByStoreId(Integer storeId,@RequestBody Commodity commodity){
        commodity.setStoreId(storeId);
        return commodityService.updCommodity(commodity);
    }

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
    @RequestMapping("/selStoreCommodityVOByStoreId")
    public ResultBean selStoreCommodityVOByStoreId(Integer storeId){
        return  ResultFactory.createSuccessResult(commodityService.selStoreCommodityVOByStoreId(storeId));
    }

    //前端接口
    //查询商品 用于 店铺修改商品
    @RequestMapping("/selCommodityByCommodityId")
    public Commodity selCommodityByCommodityId(Integer commodityId){
        return commodityService.selCommodityByCommodityId(commodityId);
    }


//***************************上面的接口 用于 页面展示商品信息***************************************************

    //判断买家是否把自己的商品添加到购物车
    @RequestMapping("isCommodityStoreEqualUser")
    public boolean isCommodityStoreEqualUser(int userId, int commodityId) {
        return commodityService.isCommodityStoreEqualUser(userId, commodityId);
    }

    //用于订单微服务 提交订单  需要的店铺id 单价  库存
    @RequestMapping("/mapCommodityToOrder")
    public  Map<Integer, CommodityToOrder> mapCommodityToOrder(@RequestBody List<Integer> commodityIdList){
        return commodityService.mapCommodityToOrder(commodityIdList);
    }

    //修改商品 库存 销量
    @PostMapping("/updCommodityNumber")
    public ResultBean updCommodityNumber(@RequestBody List<CommodityNumberChange> commodityNumberChangeList){
        return commodityService.updCommodityNumber(commodityNumberChangeList);
    }


}
