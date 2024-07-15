package com.xunmeng.jccommerce.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("批量售后请求参数")
public class BatchAfterSaleQo {

    @ApiModelProperty(value = "id", example = "HXB-HXY-007，HXB-HXY-008", required = true, position = 1)
    @NotNull(message = "id不能为空")
    @Size(min = 1, max = 100, message = "同时最多处理100个订单")
    private List<String> ids;

    @ApiModelProperty(value = "String", example = "不想要了", required = true, position = 2)
    @NotNull(message = "售后理由不能为空")
    private String reason;

    @ApiModelProperty(value = "Long", example = "10", required = true, position = 3)
    @NotNull(message = "售后金额不能为空")
    private BigDecimal amount;

}
