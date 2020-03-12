package com.cxp.shop_commodity.controller;

import com.cxp.shop_api.dto.CommodityToCart;
import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.entity.Commodity;
import com.cxp.shop_api.entity.Sort;
import com.cxp.shop_api.request.SearchRequest;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.CartCommodityVO;
import com.cxp.shop_api.vo.FavoriteCommodityVO;
import com.cxp.shop_api.vo.SearchVO;
import com.cxp.shop_api.vo.StoreCommodityVO;
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
    public boolean updCommodityOnShelvesByStoreId(Integer storeId, Integer commodityId, Boolean commodityOnShelves){
        return commodityService.updCommodityOnShelves(storeId, commodityId, commodityOnShelves);
    }

    //前端接口
    //删除商品 永久下架
    @RequestMapping("/delCommodityByStoreId")
    public boolean delCommodityByStoreId(Integer storeId, Integer commodityId){
        return commodityService.delCommodity(storeId, commodityId);
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

    //搜索商品用于生成购物车
    @RequestMapping("/getCommodityToCart")
    public CommodityToCart getCommodityToCart(Integer commodityId){
        return  commodityService.getCommodityToCart(commodityId);
    }

    //购物车 微服务 商品信息
    @RequestMapping("/mapCartCommodityVO")
    public Map<Integer, CartCommodityVO> mapCartCommodityVO(@RequestBody  List<Integer> commodityIdList){
        return  commodityService.mapCartCommodityVO(commodityIdList);
    }



    //前端接口
    //搜索页
    @RequestMapping("/getSearchVO")
    public SearchVO getSearchVO(SearchRequest searchPage_request) {
        return commodityService.getSearchVO(searchPage_request);
    }

    //前端接口
    //商品页
    @RequestMapping("/getCommodityPageByCommodityId")
    public Commodity getCommodityPageByCommodityId(Integer commodityId){
        return  commodityService.getCommodityPageByCommodityId(commodityId);
    }

    //前端接口
    //卖家中心  销售的商品
    @RequestMapping("/listStoreCommodityVOByStoreId")
    public List<StoreCommodityVO> listStoreCommodityVOByStoreId(Integer storeId){
        return  commodityService.listStoreCommodityVOByStoreId(storeId);
    }

    //前端接口
    //查询商品 用于 店铺修改商品
    @RequestMapping("/getCommodityByCommodityId")
    public Commodity getCommodityByCommodityId(Integer commodityId){
        return commodityService.getCommodityByCommodityId(commodityId);
    }


//***************************上面的接口 用于 页面展示商品信息***************************************************

    //判断买家是否把自己的商品添加到购物车
    @RequestMapping("isCommodityStoreEqualUser")
    public boolean isCommodityStoreEqualUser(int userId, int commodityId) {
        return commodityService.isCommodityStoreEqualUser(userId, commodityId);
    }

    //用于订单微服务 提交订单  需要的店铺id 单价  库存
    @RequestMapping("/getCommodityToOrder")
    public  ResultBean getCommodityToOrder(Integer userId, @RequestBody PurchaseDTO purchaseDTO){
        return commodityService.getCommodityToOrder(userId, purchaseDTO);
    }

    @RequestMapping("/listCommodityToOrder")
    public ResultBean listCommodityToOrder(Integer userId, @RequestBody List<PurchaseDTO> purchaseDTOList){
        return commodityService.listCommodityToOrder(userId, purchaseDTOList);
    }

    //修改商品 库存 销量
    @PostMapping("/subCommodityNumber")
    public ResultBean subCommodityNumber(@RequestBody List<PurchaseDTO> purchaseDTOList){
        return commodityService.subCommodityNumber(purchaseDTOList);
    }

    //交易过期 回滚商品表 商品数量
    @PostMapping("/addCommodityQuantity")
    public void addCommodityQuantity(@RequestBody PurchaseDTO purchaseDTOList){
        commodityService.addCommodityQuantity(purchaseDTOList);
    }

    @PostMapping("/addCommodityQuantityList")
    public void addCommodityQuantityList(@RequestBody List<PurchaseDTO> purchaseDTOList){
        commodityService.addCommodityQuantityList(purchaseDTOList);
    }
}
