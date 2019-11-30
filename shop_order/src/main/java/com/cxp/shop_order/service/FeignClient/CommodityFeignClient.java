package com.cxp.shop_order.service.FeignClient;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.OrderCommodityVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(value = "commodity")
public interface CommodityFeignClient {

    //更新商品库存
    @RequestMapping(value = "updCommodityNumber", method = RequestMethod.POST)
    public ResultBean updCommodityNumber(List<CommodityNumberChange> commodityNumberChangeList);

    //返回商品简单信息给 收藏夹 订单管理微服务 调用
    @PostMapping("/mapOrderCommodityVO")
    public Map<Integer, OrderCommodityVO> mapOrderCommodityVO(List<Integer> commodityIdList);
}
