package com.xunmeng.jccommerce.dto.count;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel(value = "订单销售额趋势")
public class JcOrdersSaleTendVO {

    @ApiModelProperty(value = "销售额")
    private BigDecimal allSale;

    @ApiModelProperty(value = "成本")
    private BigDecimal allCost;

    @ApiModelProperty(value = "利润")
    private BigDecimal inCome;

    @ApiModelProperty(value = "订单数")
    private Long allCount;

}

