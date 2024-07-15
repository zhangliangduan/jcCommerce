package com.xunmeng.jccommerce.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("id公共请求参数")
public class IdQo {

    @ApiModelProperty(value = "id", example = "HXB-HXY-007", required = true, position = 1)
    @NotNull(message = "id不能为空")
    private String id;

}