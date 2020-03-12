package com.cxp.shop_order.mapper;


import com.cxp.shop_api.dto.PurchaseDTO;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.vo.StoreStatusBeanVO;
import com.cxp.shop_order.pojo.AddMultipleOrderParentToTal;
import com.cxp.shop_order.pojo.AddOrderSon;
import com.cxp.shop_order.pojo.AddSingleOrderParent;
import com.cxp.shop_order.pojo.OrderPayPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper<CommodityNumberChange> {

    //父表
    public int insOrderParent(AddSingleOrderParent addSingleOrderParent);
    public int inOrderParentList(AddMultipleOrderParentToTal addMultipleOrderParentToTal);
    //子表
    public int insOrderSon(AddOrderSon addOrderSon);
    public int insOrderSonList(List<AddOrderSon> addOrderSonList);


    //拿 店铺id（收钱方）、转账金额，  用于user微服务转账
    public List<OrderPayPO> listOrderPayPO(@Param("userId")int userId, @Param("orderIdList") List<Integer> orderIdList);

    //刷新订单状态
    public int updOrderStateByUserIdOrderId(@Param("userId")int userId, @Param("orderIdList") List<Integer> orderIdList);


    //用于商品回滚库存预占 获取订单中 商品id 和 数量
    public PurchaseDTO getRollbackQuantityByOrderId(@Param("orderId") Long orderId);
    public List<PurchaseDTO> listRollbackQuantityByOrderId(@Param("orderIdList") List<Long> orderIdList);

    //修改订单 库存预占状态
    public int uptStockLockState(@Param("orderId") Long orderId);
    public int uptStockLockStateList(@Param("orderIdList") List<Long> orderIdList);


    //卖家单月 出售情况
    public StoreStatusBeanVO getStoreSalesCurrentMonth(@Param("storeId") int storeId);
    //卖家  总 出售情况
    public StoreStatusBeanVO getStoreSalesEarnings(@Param("storeId") int storeId);


    //查询该卖家所有父订单
    public List<OrderParent> listStoreOrderParent(@Param("storeId")int storeId);
    //查询该买家所有父订单        orderState 可为null
    public List<OrderParent> listUserOrderParent(@Param("userId")int userId, @Param("orderState") Integer orderState);




    //查询 某一订单 详细内容
    public OrderParent getOrderParent( @Param("orderId") int orderId);


}
