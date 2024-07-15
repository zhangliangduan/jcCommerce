package com.xunmeng.jccommerce.Converter;

import com.xunmeng.jccommerce.domain.JcOrders;
import com.xunmeng.jccommerce.domain.JcOrdersOperationEdit;
import com.xunmeng.jccommerce.dto.order.JcOrdersOperationEditVO;
import com.xunmeng.jccommerce.dto.order.JcOrdersVO;
import org.mapstruct.*;

@SuppressWarnings("unused")
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommonConverter {

    @Named("JcOrdersVOToJcOrders")
    JcOrders JcOrdersVOToJcOrders(JcOrdersVO jcOrdersvo);

    @Named("JcOrdersToJcOrdersVO")
    JcOrdersVO JcOrdersToJcOrdersVO(JcOrders jcOrders);

    @Named("JcOrdersOperationEditToJcOrdersOperationEditVO")
    JcOrdersOperationEditVO JcOrdersOperationEditToJcOrdersOperationEditVO(JcOrdersOperationEdit jcOrdersOperationEdit);
}