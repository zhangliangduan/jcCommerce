package com.xunmeng.jccommerce.dto.count;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "销售增量返回")
public class SalesDifferenceVO {

    @ApiModelProperty(value = "销售额增量")
    private BigDecimal saleDifference;

    @ApiModelProperty(value = "成本增量")
    private BigDecimal costDifference;

    @ApiModelProperty(value = "利润增量")
    private BigDecimal inComeDifference;

    @ApiModelProperty(value = "订单数增量")
    private Integer countDifference;

    @ApiModelProperty(value = "售后金额增量")
    private BigDecimal afterSaleDifference;

    @ApiModelProperty(value = "售后订单数增量")
    private Integer afterSaleCountDifference;

}
