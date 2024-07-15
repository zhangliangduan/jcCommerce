package com.xunmeng.jccommerce.service;

import com.xunmeng.jccommerce.domain.JcOrdersUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ltm
 * @since 2024-07-04 14:43:14
 */
public interface IJcOrdersUserService extends IService<JcOrdersUser> {

    /**
     * 根据用户名获取用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    JcOrdersUser getByName(String userName);
}
