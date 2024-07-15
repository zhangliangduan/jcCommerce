package com.xunmeng.jccommerce.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * mybatis-plus公共字段填充
 *
 * @author 金多虾
 * @since 2023/10/31 上午 11:38
 */
@Component
@Log4j2
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        fillValue(metaObject, CREATE_TIME, () -> getDateValue(metaObject.getSetterType(CREATE_TIME)));
        fillValue(metaObject, UPDATE_TIME, () -> getDateValue(metaObject.getSetterType(UPDATE_TIME)));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        fillValue(metaObject, UPDATE_TIME, () -> getDateValue(metaObject.getSetterType(UPDATE_TIME)));
    }

    private void fillValue(MetaObject metaObject, String fieldName, Supplier<Object> valueSupplier) {
        if (!metaObject.hasGetter(fieldName)) {
            return;
        }
        Object sidObj = metaObject.getValue(fieldName);
        if (sidObj == null && metaObject.hasSetter(fieldName) && valueSupplier != null) {
            setFieldValByName(fieldName, valueSupplier.get(), metaObject);
        }
    }

    private Object getDateValue(Class<?> setterType) {
        if (LocalDateTime.class.equals(setterType)) {
            return LocalDateTime.now();
        }
        if (Long.class.equals(setterType)) {
            return System.currentTimeMillis() / 1000;
        }
        if (Integer.class.equals(setterType)) {
            return (int) (System.currentTimeMillis() / 1000);
        }
        return null;
    }
}

