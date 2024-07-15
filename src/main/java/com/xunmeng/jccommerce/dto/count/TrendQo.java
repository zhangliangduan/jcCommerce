package com.xunmeng.jccommerce.dto.count;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel("图表公共请求参数")
public class TrendQo {

    @ApiModelProperty(value = "Date", example = "2024-01-01", required = true, position = 1)
    @NotNull(message = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "Date", example = "2024-01-01", required = true, position = 2)
    @NotNull(message = "结束日期")
    private Date endDate;

}
