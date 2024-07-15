package com.xunmeng.jccommerce.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户名密码登录请求参数
 *
 * @author ltm
 * @since 2024/7/4
 */

@Data
@ApiModel(value = "用户名密码登录请求参数")
public class UserNameLoginQo {

    @ApiModelProperty(value = "用户名", example = "test", required = true, dataType = "String")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "密码", example = "123456xm", required = true, dataType = "String")
    @NotBlank(message = "密码不能为空")
    private String pwd;
}
