package com.xunmeng.jccommerce.annotation;


import java.lang.annotation.*;

/**
 * 基于SpringBoot和Redis实现Token权限认证
 *
 * @author ltm
 * @since 2024/7/4 13:58
 */

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthToken {

}
