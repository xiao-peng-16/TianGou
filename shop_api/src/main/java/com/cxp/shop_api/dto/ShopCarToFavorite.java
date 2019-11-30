package com.cxp.shop_api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShopCarToFavorite {

    List<Integer> shopCarIdList;
    List<Integer> commodityIdList;

}
