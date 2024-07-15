package com.xunmeng.jccommerce.service.impl;

import com.xunmeng.jccommerce.domain.JcOrdersOperationEdit;
import com.xunmeng.jccommerce.mapper.JcOrdersOperationEditMapper;
import com.xunmeng.jccommerce.service.IJcOrdersOperationEditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ltm
 * @since 2024-07-05 08:59:42
 */
@Service
public class JcOrdersOperationEditServiceImpl extends ServiceImpl<JcOrdersOperationEditMapper, JcOrdersOperationEdit> implements IJcOrdersOperationEditService {

    @Override
    public Boolean saveRecord(String name, String type) {
        JcOrdersOperationEdit jcOrdersOperationEdit = new JcOrdersOperationEdit();
        jcOrdersOperationEdit.setName(name);
        jcOrdersOperationEdit.setOperationType(type);
        return this.save(jcOrdersOperationEdit);
    }
}
