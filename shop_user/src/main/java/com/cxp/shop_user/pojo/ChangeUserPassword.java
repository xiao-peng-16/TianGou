package com.cxp.shop_user.pojo;

import lombok.Data;

@Data
public class ChangeUserPassword {
    String oldUserPassword; //旧密码
    String newUserPassword; //新密码
}
