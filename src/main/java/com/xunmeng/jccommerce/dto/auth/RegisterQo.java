package com.xunmeng.jccommerce.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册请求参数
 *
 * @author ltm
 * @since 2024/7/4
 */
@Data
@ApiModel(value = "用户注册请求参数")
public class RegisterQo {

    @ApiModelProperty(value = "用户名", example = "xxx", required = true, dataType = "String")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 30, message = "用户名长度2-20位")
    private String name;

    @ApiModelProperty(value = "密码", example = "123456", required = true, dataType = "String")
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度8-20位")
    private String pwd;

}
