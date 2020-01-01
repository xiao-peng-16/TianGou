package com.cxp.shop_zuul.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class TokenTarget {

    Integer userId;
    Integer storeId;


    public TokenTarget(Integer userId) {
        this.userId = userId;
    }
}
