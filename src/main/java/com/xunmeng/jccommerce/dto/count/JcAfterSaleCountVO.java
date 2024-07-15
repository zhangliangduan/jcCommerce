package com.xunmeng.jccommerce.dto.count;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("售后统计")
public class JcAfterSaleCountVO {

    @ApiModelProperty(value = "售出金额")
    private BigDecimal allSale;

    @ApiModelProperty(value = "售后件数")
    private Integer afterSaleQuantity;

    @ApiModelProperty(value = "售出个数")
    private Integer saleQuantity;

    @ApiModelProperty(value = "售后率")
    private BigDecimal afterSaleRate;

}
