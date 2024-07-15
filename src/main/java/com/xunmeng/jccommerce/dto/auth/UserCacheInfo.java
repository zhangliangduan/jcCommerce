package com.xunmeng.jccommerce.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息
 *
 * @author ltm
 * @since 2024/7/4
 */
@Data
public class UserCacheInfo {

    @ApiModelProperty(value = "userId")
    private Long userId= 0L;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty("是否为管理员")
    private Integer isAdmin;

    @ApiModelProperty(value = "数据访问token")
    private String tokenWEB;
}
