package com.xunmeng.jccommerce.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("id公共请求参数")
public class AfterQo {

    @ApiModelProperty(value = "id", example = "HXB-HXY-007", required = true, position = 1)
    @NotNull(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "String", example = "不想要了", required = true, position = 2)
    @NotNull(message = "id不能为空")
    private String afterSaleReason;

    @ApiModelProperty(value = "Long", example = "10", required = true, position = 3)
    @NotNull(message = "id不能为空")
    private BigDecimal afterSaleAmount;

}
