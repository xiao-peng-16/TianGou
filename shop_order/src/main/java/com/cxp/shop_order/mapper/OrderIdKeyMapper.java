package com.cxp.shop_order.mapper;

import com.cxp.shop_api.entity.OrderParent;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class OrderIdKeyMapper extends SqlSessionDaoSupport {
    @SuppressWarnings("rawtypes")
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    //查询  出订单 对应商品id  ，拦截器内设置 每天订单商品id最多4个 ，因为前端展示3个就好了 后面省略号
    @SuppressWarnings("rawtypes")
    public Map<Integer, List<Integer>> getOrderIdCommodityIdMap( List<OrderParent> orderParentList){
        OrderIdCommodityIdHandler orderIdCommodityIdHandler = new OrderIdCommodityIdHandler();
        this.getSqlSession().select("com.cxp.shop_order.mapper.OrderIdKeyMapper.selOrderIdCommodityId",
                orderParentList,orderIdCommodityIdHandler);
        return orderIdCommodityIdHandler.getOrderIdCommodityIdMap();
    }

}
