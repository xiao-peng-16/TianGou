package com.cxp.shop_order.mapper;

import com.cxp.shop_order.pojo.OrderIdCommodityId;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderIdCommodityIdHandler implements ResultHandler {
    //拦截器内设置 每天订单商品id最多4个 ，因为前端展示3个就好了 后面省略号
//    private final int MAX = 4;

    private final Map<Integer, List<Integer>> orderIdCommodityIdMap = new HashMap<>();

    public Map<Integer, List<Integer>> getOrderIdCommodityIdMap(){
        return this.orderIdCommodityIdMap;
    }

    //对返回数据拦截 改造
    //拦截器内设置 每天订单商品id最多MAX个 ，因为前端展示MAX个就好了 后面省略号
    @Override
    public void handleResult(ResultContext resultContext) {
        OrderIdCommodityId orderIdCommodityId = (OrderIdCommodityId)resultContext.getResultObject();
        List<Integer> commodityIdList = orderIdCommodityIdMap.get(orderIdCommodityId.getOrderId());
        if (null == commodityIdList){
            commodityIdList = new ArrayList<>();
            commodityIdList.add(orderIdCommodityId.getCommodityId());
            orderIdCommodityIdMap.put(orderIdCommodityId.getOrderId(),commodityIdList);
        }else
//            if (MAX > commodityIdList.size())//最多MAX个
            commodityIdList.add(orderIdCommodityId.getCommodityId());
    }

}
