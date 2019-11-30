package com.cxp.shop_store.service.impl;

import com.cxp.shop_api.dto.StoreToCommodity;
import com.cxp.shop_api.entity.Store;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_store.mapper.StoreMapper;
import com.cxp.shop_store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreMapper storeMapper;

    static final ResultBean STORE_ID_ERROR = ResultFactory.createFailResult(ResultStatus.STORE_ID_ERROR);


    @Override
    public Map<Integer,StoreToCommodity> mapStoreToCommodityByStoreId(List<Integer> storeIdList) {
        return storeMapper.mapStoreToCommodityByStoreId(storeIdList);
    }

    @Override
    public Map<Integer, StoreToCommodity> selStoreToCommodityMapByStoreName(String searchWord) {
        return searchWord == null? null: storeMapper.selStoreToCommodityMapByStoreName(searchWord);
    }


    @Override
    public Store selStoreByStoreId(Integer storeId) {
        return storeMapper.selStoreByStoreId(storeId);
    }
}
