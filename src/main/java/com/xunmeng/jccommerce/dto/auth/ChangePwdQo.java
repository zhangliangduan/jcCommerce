package com.xunmeng.jccommerce.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "用户修改密码请求参数")
public class ChangePwdQo {

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")
    @ApiModelProperty(value = "密码", example = "xunmeng", required = true)
    private String pwd;

}
