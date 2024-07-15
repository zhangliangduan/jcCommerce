package com.xunmeng.jccommerce.service;

import com.xunmeng.jccommerce.domain.JcOrdersOperationEdit;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ltm
 * @since 2024-07-05 08:59:42
 */
public interface IJcOrdersOperationEditService extends IService<JcOrdersOperationEdit> {

    /**
     * 保存操作记录
     * @param name 操作人
     * @param type 操作类型
     * @return 是否成功
     */
    Boolean saveRecord(String name, String type);
}
