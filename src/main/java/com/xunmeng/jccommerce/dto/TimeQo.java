package com.xunmeng.jccommerce.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("下载公共请求参数")
@Data
public class TimeQo {

    @ApiModelProperty(value = "String", example = "E:\\30.xlsx", position = 1)
    private String req;

}
