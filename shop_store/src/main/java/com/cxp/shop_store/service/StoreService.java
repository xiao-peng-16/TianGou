package com.cxp.shop_store.service;

import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.dto.UserOpenStoreDTO;
import com.cxp.shop_api.entity.Store;
import com.cxp.shop_api.result.ResultBean;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

public interface StoreService {

    //用于 网关 检测用户对应的 店铺id
    public Integer selStoreIdByUserId(Integer userId);

    //用户 开通店铺功能
    public ResultBean addStoreIdByUserId(UserOpenStoreDTO userOpenStoreDTO);

    //判断用户名是否可以注册 (防止已经有人注册过了)
    public boolean isUsableStoreName(String storeName);

    //返回店铺完整信息   用于组成店铺页的店铺信息
    public Store selStoreByStoreId(Integer userId);

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息
    public Map<Integer,StoreToCommodity> mapStoreToCommodityByStoreId(List<Integer> storeIdList);

    //返回店铺大致信息：名字、地点 用于组成搜索页商品的店铺信息  根据店铺名字 模糊查询
    public Map<Integer,StoreToCommodity> selStoreToCommodityMapByStoreName(String searchWord);



    //查询一组店铺名 根据id
    public Map<Integer,String> mapStoreNameByStoreId(List<Integer> storeIdList);

    //查询店铺名 根据id
    public String  selStoreNameByStoreId(Integer storeId);



}
