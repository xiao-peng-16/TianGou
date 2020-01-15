package com.cxp.shop_car.service.FeignClient;

import com.cxp.shop_api.vo.ShopCarCommodityVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "commodity")
public interface CommodityFeignClient {


    //返回商品简单信息给 收藏夹 订单管理微服务 调用
    @RequestMapping(value = "/mapShopCarCommodityVO", method = RequestMethod.POST)
    public Map<Integer, ShopCarCommodityVO> mapShopCarCommodityVO(List<Integer> commodityIdList);



    @RequestMapping(value = "isCommodityStoreEqualUser", method = RequestMethod.GET)
    public boolean isCommodityStoreEqualUser(@RequestParam int userId, @RequestParam int commodityId);
}
