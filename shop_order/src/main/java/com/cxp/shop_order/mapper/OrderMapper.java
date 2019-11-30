package com.cxp.shop_order.mapper;


import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.entity.OrderSon;
import com.cxp.shop_api.vo.OrderGeneraVO;
import com.cxp.shop_api.vo.StoreStatusBeanVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderMapper<CommodityNumberChange> {

    //父表
    public int insOrderParent(OrderParent orderParent);
    //子表
    public int insOrderSon(@Param("orderId") int orderId, @Param("orderSonList") List<OrderSon> orderSonList);
    //查询用户MoneyChange 安全验证
    public MoneyChange selUserMoneyChangeByOrderId(@Param("orderId")int orderId);
    //拿 店铺id（收钱方）、转账金额，  用于user微服务转账
    public LinkedList<MoneyChange> selAllMoneyChangeByOrderId(@Param("orderId")int orderId);
    //刷新支付状态
    @Update("update order_parent set order_submit = true where order_id = #{orderId} ")
    public int updSubmintByOrderId(@Param("orderId")int orderId);
    //获取订单中 商品id 和 数量
    public List<CommodityNumberChange> selChooseNumber(@Param("orderId")int orderId);



    //卖家单月 出售情况
    public StoreStatusBeanVO selStoreSalesCurrentMonth(@Param("storeId") int storeId);
    //卖家  总 出售情况
    public StoreStatusBeanVO selStoreSalesEarnings(@Param("storeId") int storeId);


    //查询该卖家所有订单 总体内容
    public List<OrderGeneraVO> listStoreOrderGeneraVO(@Param("storeId")int storeId);
    //查询该买家所有订单 总体内容
    public List<OrderGeneraVO> listUserOrderGeneraVO(@Param("userId")int userId);


    //返回订单粗略大致内容
    @MapKey("orderId")
    public Map<Integer, OrderParent> selOrderParentMap(List<OrderGeneraVO> orderGeneraList);


    //查询该卖家 某一订单 详细内容
    public OrderParent selStoreOrderParent(@Param("storeId") int storeId, @Param("orderId") int orderId);
    //查询该买家 某一订单 详细内容
    public OrderParent selUserOrderParent(@Param("userId") int userId, @Param("orderId") int orderId);


}
