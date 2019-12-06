package com.cxp.shop_store.controller;

import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.entity.Store;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_store.service.impl.StoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*
*   店铺
* */
@CrossOrigin(allowCredentials = "true")
@RestController
public class StoreController {

    @Autowired
    StoreServiceImpl storeService;

    //前端接口
    //返回店铺完整信息   用于组成店铺页的店铺信息
    @RequestMapping("selStoreByUserId")
    public ResultBean selStoreByUserId(Integer userId){
        return ResultFactory.createSuccessResult(storeService.selStoreByStoreId(userId));
    }

    //返回店铺完整信息   用于组成商品页的店铺信息
    @RequestMapping("selStoreByStoreId")
    public Store selStoreByStoreId(Integer storeId){
        return storeService.selStoreByStoreId(storeId);
    }


    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺名字 模糊查询
    @RequestMapping("selStoreToCommodityMapByStoreName")
    public Map<Integer,StoreToCommodity> selStoreToCommodityMapByStoreName(String searchWord){
        return storeService.selStoreToCommodityMapByStoreName(searchWord);
    }

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺id
    @PostMapping("mapStoreToCommodityByStoreId")
    public Map<Integer,StoreToCommodity> mapStoreToCommodityByStoreId(@RequestBody List<Integer> storeIdList){
        return storeService.mapStoreToCommodityByStoreId(storeIdList);
    }

    //查询一组店铺名 根据id
    @PostMapping("/mapStoreNameByStoreId")
    public Map<Integer,String> mapStoreNameByStoreId(@RequestBody List<Integer> storeIdList){
        return storeService.mapStoreNameByStoreId(storeIdList);
    }

    //查询店铺名 根据id
    @RequestMapping("/selStoreNameByStoreId")
    public String  selStoreNameByStoreId(Integer storeId){
        return storeService.selStoreNameByStoreId(storeId);
    }




}
