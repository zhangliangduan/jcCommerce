package com.xunmeng.jccommerce.service.impl;

import com.xunmeng.jccommerce.domain.JcOrdersUser;
import com.xunmeng.jccommerce.mapper.JcOrdersUserMapper;
import com.xunmeng.jccommerce.service.IJcOrdersUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ltm
 * @since 2024-07-04 14:43:14
 */
@Service
public class JcOrdersUserServiceImpl extends ServiceImpl<JcOrdersUserMapper, JcOrdersUser> implements IJcOrdersUserService {

    @Override
    public JcOrdersUser getByName(String userName) {
        return this.lambdaQuery()
                .eq(JcOrdersUser::getName, userName)
                .select(JcOrdersUser::getId, JcOrdersUser::getName, JcOrdersUser::getPwd, JcOrdersUser::getSalt)
                .one();
    }
}
