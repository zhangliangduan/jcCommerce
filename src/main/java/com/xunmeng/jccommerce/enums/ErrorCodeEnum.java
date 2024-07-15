package com.xunmeng.jccommerce.enums;

import lombok.Getter;

/**
 * 错误码
 *
 * @author ltm
 * @since 2024/4/29
 */
@Getter
public enum ErrorCodeEnum {

    SUCCESS(0, "success"),
    FAIL(1, "fail"),
    PARAM_ERROR(2, "param error"),
    LOGIN_INVALID(75, "登陆失效"),
    PERMISSION_DENIED(3, "permission denied"),


    /**
     * 系统错误
     */
    SYSTEM_ERROR(500, "系统错误，请联系后台管理员");

    private final int code;
    private final String desc;

    ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
