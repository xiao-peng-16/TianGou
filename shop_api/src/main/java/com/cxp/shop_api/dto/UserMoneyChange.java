package com.cxp.shop_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserMoneyChange {
    int userId;
    double moneyChange;

}
