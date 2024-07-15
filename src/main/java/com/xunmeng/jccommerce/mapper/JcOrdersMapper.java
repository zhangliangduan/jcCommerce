package com.xunmeng.jccommerce.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunmeng.jccommerce.domain.JcOrders;
import com.xunmeng.jccommerce.dto.count.JcOrdersCount;
import com.xunmeng.jccommerce.dto.order.JcAmount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author zld
 */
public interface JcOrdersMapper extends BaseMapper<JcOrders> {

    /**
     * 获取某段时间内，某手机号的购买次数
     *
     * @param startTime 开始时间
     * @param phones    手机号
     * @return map
     */
    List<JcOrdersCount> getPurchasesNumbers(@Param("startTime") long startTime, @Param("phones") Set<String> phones);

    JcAmount getSaleAmount(@Param("start") long startTime, @Param("end") long endTime, @Param("type") Integer type);

    BigDecimal getAfterSaleAmount(@Param("start") Long start, @Param("end") Long end);

    @Select("SELECT COUNT(*) " +
            "FROM jc_orders " +
            "WHERE is_after_sale = 1 " +
            "AND create_time <= #{start} AND create_time >= #{end}")
    Integer sumAfterSaleQuantity(@Param("start") long start, @Param("end") long end);

    @Select("SELECT COUNT(*) " +
            "FROM jc_orders " +
            "WHERE create_time <= #{start} AND create_time >= #{end}")
    Integer sumAllQuantity(@Param("start") long start, @Param("end") long end);
}




