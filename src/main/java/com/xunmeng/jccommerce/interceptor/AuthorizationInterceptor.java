package com.xunmeng.jccommerce.interceptor;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xunmeng.jccommerce.annotation.AuthToken;
import com.xunmeng.jccommerce.constant.CacheConstant;
import com.xunmeng.jccommerce.constant.LoginConstant;
import com.xunmeng.jccommerce.dto.auth.UserCacheInfo;
import com.xunmeng.jccommerce.enums.ConstantEnum;
import com.xunmeng.jccommerce.enums.ErrorCodeEnum;
import com.xunmeng.jccommerce.util.RedisUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 权限拦截器
 *
 * @author ltm
 * @since 2024/7/4
 */

@Log4j2
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final RedisUtil redisUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        try {
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            MDC.put("traceId", IdUtil.fastSimpleUUID());
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            // 如果打上了AuthToken注解则需要验证token
            if (method.getAnnotation(AuthToken.class) == null && handlerMethod.getBeanType().getAnnotation(AuthToken.class) == null) {
                request.setAttribute(ConstantEnum.REQUEST_CURRENT_USER_KEY, null);
                request.setAttribute(ConstantEnum.REQUEST_CURRENT_USER_INFO_KEY, null);
                return true;
            }

            String jwtToken = request.getHeader(LoginConstant.TOKEN);
            if (StrUtil.isBlank(jwtToken)) {
                this.buildResponse(response, ErrorCodeEnum.LOGIN_INVALID);
                return false;
            }
            //从jwt中解出userId token
            JWT jwt = JWTUtil.parseToken(jwtToken);
            if (!jwt.setKey(LoginConstant.JWT_KEY).verify()) {
                this.buildResponse(response, ErrorCodeEnum.LOGIN_INVALID);
                return false;
            }
            String token = Objects.isNull(jwt.getPayload("token")) ? "" : jwt.getPayload("token").toString();
            long userId = Objects.isNull(jwt.getPayload("userId")) ? 0L : Long.parseLong(jwt.getPayload("userId").toString());
            if (userId == 0L || StrUtil.isBlank(token)) {
                this.buildResponse(response, ErrorCodeEnum.LOGIN_INVALID);
                return false;
            }
            String userInfoStr = redisUtil.get(CacheConstant.USER_INFO + userId);
            if (Objects.isNull(userInfoStr)) {
                this.buildResponse(response, ErrorCodeEnum.LOGIN_INVALID);
                return false;
            }
            UserCacheInfo user = JSON.parseObject(userInfoStr, UserCacheInfo.class);
            if (Objects.isNull(user)) {
                this.buildResponse(response, ErrorCodeEnum.LOGIN_INVALID);
                return false;
            }
            // token已被更新,其他地方已登录
            if (!Objects.equals(token, user.getTokenWEB())) {
                this.buildResponse(response, ErrorCodeEnum.LOGIN_INVALID);
                return false;
            }
            // 把登录信息放进上下文中
            request.setAttribute(ConstantEnum.REQUEST_CURRENT_USER_KEY, user.getUserId());
            request.setAttribute(ConstantEnum.REQUEST_CURRENT_USER_INFO_KEY, user);
            return true;
        } catch (Exception e) {
            log.error("权限拦截器异常", e);
            this.buildResponse(response, ErrorCodeEnum.SYSTEM_ERROR);
        }
        return false;
    }

    /**
     * 构建返回流
     *
     * @param response  默认返回信息
     * @param errorCode 自定义错误状态枚举
     */
    private void buildResponse(HttpServletResponse response, ErrorCodeEnum errorCode) {
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            jsonObject.put("code", errorCode.getCode());
            jsonObject.put("msg", errorCode.getDesc());
            out = response.getWriter();
            out.println(jsonObject);
        } catch (Exception ex) {
            log.error("AuthorizationInterceptor preHandle error: {}", ex.getMessage());
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        MDC.clear();
    }
}
