package com.xunmeng.jccommerce.config;

import com.xunmeng.jccommerce.base.BusinessException;
import com.xunmeng.jccommerce.base.Result;
import com.xunmeng.jccommerce.enums.ErrorCodeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常统一处理类
 *
 * @author ltm
 * @since 2024/4/29
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Object> businessException(BusinessException e) {
        Result<Object> response = new Result<>();
        response.setCode(e.getErrorCodeEnum().getCode());
        response.setMsg(e.getMsg());
        return response;
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> businessException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMsg = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return Result.newFailedResponse(ErrorCodeEnum.PARAM_ERROR,
                errorMsg);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return Result.newFailedResponse(ErrorCodeEnum.SYSTEM_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.newFailedResponse(ErrorCodeEnum.SYSTEM_ERROR, e.getMessage());
    }

}
