package com.cxp.shop_order.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddMultipleOrderParentToTal {

    int userId;                  //用户id
    String userName;             //买家用户名
    String orderTime;            //'时间'
    long stockLockExpiration;    //'提示过期时间'

    List<AddMultipleOrderParent> addMultipleOrderParentList;
}
