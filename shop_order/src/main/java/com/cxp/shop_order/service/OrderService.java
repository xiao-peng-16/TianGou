package com.cxp.shop_order.service;


import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.OrderGeneraVO;
import com.cxp.shop_api.vo.StoreStatusFullVO;
import com.cxp.shop_order.eception.AddOrderException;

import java.util.List;

public interface OrderService {


    //添加订单
    public ResultBean addOrder(OrderParent orderParent) throws AddOrderException;
    //支付订单
    public ResultBean payOrderByUserId(int userId,int orderId);

    //查询店铺状态
    public StoreStatusFullVO selStoreStatusFullVO(Integer storeId);

    //查询该卖家所有订单 总体内容
    public List<OrderGeneraVO> listStoreOrderGeneraVO(Integer storeId);
    //查询该卖家某一个订单 详细内容
    public OrderParent selStoreOrderParent(Integer userId, Integer orderId);
    //查询该买家所有订单 总体内容
    public List<OrderGeneraVO> listUserOrderGeneraVO(Integer userId);

}
