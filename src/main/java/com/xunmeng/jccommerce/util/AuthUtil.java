package com.xunmeng.jccommerce.util;

import com.xunmeng.jccommerce.dto.auth.UserCacheInfo;
import com.xunmeng.jccommerce.enums.ConstantEnum;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 认证信息工具类
 *
 * @author ltm
 * @since 2024/7/4
 */
@SuppressWarnings("unused")
public class AuthUtil {

    /**
     * 获取当前登录用户userId
     *
     * @return userId
     */
    public static long getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return (Long) request.getAttribute(ConstantEnum.REQUEST_CURRENT_USER_KEY);
    }

    public static UserCacheInfo getCurrentUserInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return (UserCacheInfo) request.getAttribute(ConstantEnum.REQUEST_CURRENT_USER_INFO_KEY);
    }

}
