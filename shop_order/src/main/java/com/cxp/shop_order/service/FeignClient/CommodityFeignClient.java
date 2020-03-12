package com.cxp.shop_order.service.FeignClient;

import com.cxp.shop_api.dto.CommodityNumberChange;
import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_order.pojo.AddOrderListSonResultBean;
import com.cxp.shop_order.pojo.AddOrderSonResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "commodity")
public interface CommodityFeignClient {

    //用于订单微服务 提交订单  需要的店铺id 单价  库存

    @RequestMapping(value = "/getCommodityToOrder", method = RequestMethod.GET)
    public AddOrderSonResultBean getCommodityToOrder(@RequestParam Integer userId, PurchaseDTO purchaseDTO);

    @RequestMapping(value = "/listCommodityToOrder", method = RequestMethod.POST)
    public AddOrderListSonResultBean listCommodityToOrder(@RequestParam Integer userId, List<PurchaseDTO> purchaseDTOList);

    //更新商品库存
    @RequestMapping(value = "subCommodityNumber", method = RequestMethod.POST)
    public ResultBean subCommodityNumber(List<CommodityNumberChange> commodityNumberChangeList);


    //交易过期 回滚商品表 商品数量
    @RequestMapping(value = "/addCommodityQuantity", method = RequestMethod.POST)
    public void addCommodityQuantity(PurchaseDTO purchaseDTOList);

    @RequestMapping(value = "/addCommodityQuantityList", method = RequestMethod.POST)
    public void addCommodityQuantityList(List<PurchaseDTO> purchaseDTOList);

}
