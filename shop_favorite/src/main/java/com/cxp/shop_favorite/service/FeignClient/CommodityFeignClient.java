package com.cxp.shop_favorite.service.FeignClient;

import com.cxp.shop_api.vo.FavoriteCommodityVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "commodity")
public interface CommodityFeignClient {


    //返回商品简单信息给 收藏夹 订单管理微服务 调用
    @PostMapping("/mapFavoriteCommodityVO")
    public Map<Integer, FavoriteCommodityVO> mapFavoriteCommodityVO(List<Integer> commodityIdList);
}
