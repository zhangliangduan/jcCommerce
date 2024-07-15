package com.xunmeng.jccommerce.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "销售金额接收")
public class JcAmount {

    @ApiModelProperty(value = "销售金额", example = "3", required = true, position = 1)
    private BigDecimal totalSale;
}
