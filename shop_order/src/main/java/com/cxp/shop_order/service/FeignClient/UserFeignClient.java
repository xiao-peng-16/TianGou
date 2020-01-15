package com.cxp.shop_order.service.FeignClient;


import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "user")
public interface UserFeignClient {

    //查询用户名根据id
    @RequestMapping(value = "/mapUserNameByUserId", method = RequestMethod.POST)
    public Map<Integer,String> mapUserNameByUserId(List<Integer> userIdList);

    //查询用户名 根据id
    @RequestMapping(value = "/getUserNameByUserId", method = RequestMethod.GET)
    public String  getUserNameByUserId(@RequestParam Integer userId);

    //转账
    @RequestMapping(value = "shopTransferByUserId", method = RequestMethod.POST)
    public ResultBean shopTransferByUserId(List<MoneyChange> moneyChangeLinkedList);
}
