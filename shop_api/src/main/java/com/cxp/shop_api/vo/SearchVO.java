package com.cxp.shop_api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchVO {

    int commoditySum;
    List<SearchCommodityVO> searchCommodityVOList;

}
