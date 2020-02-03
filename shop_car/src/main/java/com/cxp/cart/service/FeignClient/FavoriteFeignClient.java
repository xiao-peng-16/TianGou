package com.cxp.cart.service.FeignClient;

import com.cxp.shop_api.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "favorite")
public interface FavoriteFeignClient {

    @RequestMapping(value = "addFavoriteByUserId", method = RequestMethod.POST)
    public ResultBean addFavoriteByUserId(@RequestParam int userId, @RequestBody List<Integer> commodityIdList);
}
