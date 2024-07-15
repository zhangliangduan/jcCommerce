package com.xunmeng.jccommerce.base;

import com.xunmeng.jccommerce.enums.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局异常统一处理类
 *
 * @author ltm
 * @since 2024/4/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class BusinessException extends RuntimeException {

    private ErrorCodeEnum errorCodeEnum;
    private String msg;

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getDesc());
        this.errorCodeEnum = errorCodeEnum;
        this.msg = errorCodeEnum.getDesc();
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String msg) {
        super(errorCodeEnum.getDesc());
        this.errorCodeEnum = errorCodeEnum;
        this.msg = msg;
    }

}
