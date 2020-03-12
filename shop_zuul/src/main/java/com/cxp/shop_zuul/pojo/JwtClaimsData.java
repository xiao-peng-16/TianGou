package com.cxp.shop_zuul.pojo;

import lombok.Data;

@Data
public class JwtClaimsData {
    long refreshTime;
    int userId;
    Integer storeId;
}
