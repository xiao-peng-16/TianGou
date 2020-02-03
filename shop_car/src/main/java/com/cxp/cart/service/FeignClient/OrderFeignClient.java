package com.cxp.cart.service.FeignClient;


import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "order")
public interface OrderFeignClient {

    //提交订单
    @RequestMapping(value = "/submitMultipleOrderByUserId",method = RequestMethod.POST)
    public ResultBean submitMultipleOrderByUserId(@RequestParam Integer userId, List<PurchaseDTO> purchaseDTOList);
}
