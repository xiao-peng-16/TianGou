package com.cxp.shop_order.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddMultipleOrderParentToTal {

    int userId;                  //用户id
    String userName;             //买家用户名

    List<AddMultipleOrderParent> addMultipleOrderParentList;
}
