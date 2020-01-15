package com.cxp.shop_store.service.impl;

import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.dto.UserOpenStoreDTO;
import com.cxp.shop_api.entity.Store;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_store.mapper.StoreMapper;
import com.cxp.shop_store.pojo.StoreIdStoreName;
import com.cxp.shop_store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreMapper storeMapper;


    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean STORE_REGISTER_ERROR =ResultFactory.createFailResult(ResultStatus.STORE_REGISTER_ERROR);
    static final ResultBean STORE_NAME_DISABLED =ResultFactory.createFailResult(ResultStatus.STORE_NAME_DISABLED);


    @Override
    public Integer selStoreIdByUserId(Integer userId) {
        return storeMapper.selStoreIdByUserId(userId);
    }

    @Override
    public ResultBean addStoreIdByUserId(UserOpenStoreDTO userOpenStoreDTO) {
        try {
            storeMapper.addStoreIdByUserId(userOpenStoreDTO);
            return successResult;
        }catch (Exception e){
            if(e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException
                && e.getCause().toString().contains("UK_store_name"))
                return STORE_NAME_DISABLED;
            return STORE_REGISTER_ERROR;
        }
    }

    @Override
    public boolean isUsableStoreName(String storeName) {
        return 0 == storeMapper.storeNameCout(storeName);
    }

    @Override
    public Map<Integer,StoreToCommodity> mapStoreToCommodityByStoreId(List<Integer> storeIdList) {
        return storeMapper.mapStoreToCommodityByStoreId(storeIdList);
    }

    @Override
    public Map<Integer, StoreToCommodity> selStoreToCommodityMapByStoreName(String searchWord) {
        return searchWord == null? null: storeMapper.selStoreToCommodityMapByStoreName(searchWord);
    }

    @Override
    public Map<Integer, String> mapStoreNameByStoreId(List<Integer> storeIdList) {
        List<StoreIdStoreName> storeIdStoreNameList = storeMapper.mapStoreNameByStoreId(storeIdList);
        HashMap<Integer, String> map = new HashMap<>();
        for (StoreIdStoreName storeIdStoreName : storeIdStoreNameList)
            map.put(storeIdStoreName.getStoreId(),storeIdStoreName.getStoreName());
        return map;
    }

    @Override
    public String getStoreNameByStoreId(Integer storeId) {
        return storeMapper.getStoreNameByStoreId(storeId);
    }




    @Override
    public Store getStoreByStoreId(Integer storeId) {
        return storeMapper.getStoreByStoreId(storeId);
    }
}
