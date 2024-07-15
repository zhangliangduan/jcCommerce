package com.xunmeng.jccommerce.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录信息
 *
 * @author ltm
 * @since 2024/7/4
 */
@Data
public class LoginVo {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "token")
    private String token;
}
