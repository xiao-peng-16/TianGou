package com.cxp.cart.service.FeignClient;

import com.cxp.shop_api.dto.CommodityToCart;
import com.cxp.shop_api.vo.CartCommodityVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "commodity")
public interface CommodityFeignClient {

    //搜索商品用于生成购物车
    @RequestMapping(value = "/getCommodityToCart",method = RequestMethod.GET)
    public CommodityToCart getCommodityToCart(@RequestParam Integer commodityId);

    //返回商品简单信息给 收藏夹 订单管理微服务 调用
    @RequestMapping(value = "/mapCartCommodityVO", method = RequestMethod.POST)
    public Map<Integer, CartCommodityVO> mapCartCommodityVO(List<Integer> commodityIdList);



    @RequestMapping(value = "isCommodityStoreEqualUser", method = RequestMethod.GET)
    public boolean isCommodityStoreEqualUser(@RequestParam int userId, @RequestParam int commodityId);
}
