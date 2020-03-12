package com.cxp.shop_store.controller;

import com.cxp.shop_api.entity.Store;
import com.cxp.shop_api.entity.StoreBase;
import com.cxp.shop_api.result.ResultBean;
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


    //用于 网关 检测用户对应的 店铺id
    @RequestMapping("selStoreIdByUserId")
    public Integer selStoreIdByUserId(Integer userId){
        return storeService.selStoreIdByUserId(userId);
    }

    //前端接口
    //用户 开通店铺功能
    @RequestMapping("addStoreIdByUserId")
    public ResultBean addStoreIdByUserId(StoreBase storeBase){
        return storeService.addStoreIdByUserId(storeBase);
    }

    //判断用户名是否可以注册 (防止已经有人注册过了)
    @RequestMapping("/isUsableStoreName")
    public boolean isUsableStoreName(String storeName) {
        return storeService.isUsableStoreName(storeName);
    }


    //前端接口
    //返回店铺完整信息   用于组成商品页的店铺信息
    @RequestMapping("getStoreByStoreId")
    public Store getStoreByStoreId(Integer storeId){
        return storeService.getStoreByStoreId(storeId);
    }


    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺名字 模糊查询
    @RequestMapping("selStoreBaseMapByStoreName")
    public Map<Integer,StoreBase> selStoreBaseMapByStoreName(String searchWord){
        return storeService.selStoreBaseMapByStoreName(searchWord);
    }

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺id
    @PostMapping("mapStoreBaseByStoreId")
    public Map<Integer,StoreBase> mapStoreBaseByStoreId(@RequestBody List<Integer> storeIdList){
        return storeService.mapStoreBaseByStoreId(storeIdList);
    }

    //查询一组店铺名 根据id
    @PostMapping("/mapStoreNameByStoreId")
    public Map<Integer,String> mapStoreNameByStoreId(@RequestBody List<Integer> storeIdList){
        return storeService.mapStoreNameByStoreId(storeIdList);
    }

    //查询用户id 根据店铺id
    @PostMapping("/mapUserIdByStoreId")
    public Map<Integer,Integer> mapUserIdByStoreId(@RequestBody List<Integer> storeIdList){
        return storeService.mapUserIdByStoreId(storeIdList);
    }

    //查询店铺名 根据id
    @RequestMapping("/getStoreNameByStoreId")
    public String  getStoreNameByStoreId(Integer storeId){
        return storeService.getStoreNameByStoreId(storeId);
    }




}
