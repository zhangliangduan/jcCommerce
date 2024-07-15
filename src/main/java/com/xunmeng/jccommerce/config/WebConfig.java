package com.xunmeng.jccommerce.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xunmeng.jccommerce.interceptor.AuthorizationInterceptor;
import com.xunmeng.jccommerce.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义拦截器
 *
 * @author ltm
 * @since 2024/7/4
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RedisUtil redisUtil;

    public WebConfig(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Bean
    public HandlerInterceptor getAuthHandlerInterceptor() {
        return new AuthorizationInterceptor(redisUtil);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthHandlerInterceptor())
                .excludePathPatterns("/swagger-resources", "/v2/api-docs");
    }

    /**
     * 返回到前端的json值为空的属性不显示null
     *
     * @return bean
     */
    @Bean
    public Jackson2ObjectMapperBuilder customObjectMapper() {
        Jackson2ObjectMapperBuilder mapper = new Jackson2ObjectMapperBuilder();
        mapper.serializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
