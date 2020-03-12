package com.cxp.shop_order.rabbit;

import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_order.mapper.OrderMapper;
import com.cxp.shop_order.service.FeignClient.CommodityFeignClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RabbitListener(queues = "order.queue")
public class RabbitConsumer {



    @Autowired
    OrderMapper orderMapper;
    @Autowired
    CommodityFeignClient commodityFeignClient;

    @RabbitHandler
    public void process(Long orderId) {
        System.out.println("DirectReceiver消费者收到消息Map  : " + orderId);

        PurchaseDTO rollbackQuantityByOrderId = orderMapper.getRollbackQuantityByOrderId(orderId);
        if (rollbackQuantityByOrderId == null)
            return;

        orderMapper.uptStockLockState(orderId);
        commodityFeignClient.addCommodityQuantity(rollbackQuantityByOrderId);

    }

    @RabbitHandler
    public void process(List<Long> orderIdList) {
        System.out.println("DirectReceiver消费者收到消息Map  : " + orderIdList);

        List<PurchaseDTO> purchaseDTOList = orderMapper.listRollbackQuantityByOrderId(orderIdList);
        if (purchaseDTOList.size() == 0)
            return;

        orderMapper.uptStockLockStateList(orderIdList);
        commodityFeignClient.addCommodityQuantityList(purchaseDTOList);

    }

//    @RabbitHandler
//    public void process(Object orderId) {
//        System.out.println("DirectReceiver消费者收到消息Map  : " + orderId);
//    }
}
