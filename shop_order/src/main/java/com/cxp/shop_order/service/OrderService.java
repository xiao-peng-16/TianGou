package com.cxp.shop_order.service;


import com.cxp.shop_api.entity.OrderParent;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.vo.StoreStatusFullVO;
import com.cxp.shop_order.eception.CommodityNotFound_exception;
import com.cxp.shop_order.eception.CommodityStockInsufficientException;
import com.cxp.shop_order.pojo.AddOrderSon;

import java.util.List;

public interface OrderService {


    //添加订单
    public long submitSingleOrder(Integer userId, AddOrderSon addOrderSon) throws CommodityNotFound_exception, CommodityStockInsufficientException;
    public List<Long> submitMultipleOrder(Integer userId, List<AddOrderSon> addOrderSonList) throws CommodityNotFound_exception, CommodityStockInsufficientException;
    //支付订单
    public ResultBean payOrderByUserId(int userId, List<Long> orderIdList);

    //查询店铺状态
    public StoreStatusFullVO getStoreStatusFullVO(Integer storeId);




    //查询该卖家所有订单 总体内容
    public List<OrderParent> listStoreOrderParent(Integer storeId);
    //查询该用户所有订单 总体内容
    public List<OrderParent> listUserOrderParent(Integer userId, Integer orderState);


    //某一个订单 详细内容
    public OrderParent getOrderParent(Integer orderId);



}
