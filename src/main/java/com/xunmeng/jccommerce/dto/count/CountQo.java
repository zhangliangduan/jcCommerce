package com.xunmeng.jccommerce.dto.count;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel("查询天数参数")
@Data
public class CountQo {

    @ApiModelProperty(value = "从1开始的天数，传1即为今天", example = "3", required = true, position = 1)
    @NotNull(message = "天数不能为空")
    private Integer day;

}
