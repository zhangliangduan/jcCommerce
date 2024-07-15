package com.xunmeng.jccommerce.service;

import com.xunmeng.jccommerce.domain.JcOrders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmeng.jccommerce.dto.order.JcAmount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ltm
 * @since 2024-06-25 10:53:40
 */
public interface IJcOrdersService extends IService<JcOrders> {


    /**
     * 获取已存在的ID列表
     *
     * @return List<String> 返回一个字符串列表，包含所有已存在的ID。如果不存在任何已知ID，返回空列表。
     */
    List<String> getExistingOrderIds();

    /**
     * 获取购买次数
     *
     * @param phones 客户电话列表
     * @return Map<String, Long> 返回一个映射，其中键是客户电话，值是购买次数。
     */
    Map<String, Long> getPhonesPurchaseMap(Set<String> phones);


    /**
     * 获取销售金额
     *
     * @return List<JcOrders> 返回订单列表
     */
    JcAmount getSaleAmount(Long start, Long end, Integer type);

    /**
     * 获取售出金额
     *
     * @return BigDecimal 返回售出金额
     */
    BigDecimal getAfterSaleAmount(Long start, Long end);

    /**
     * 获取成本金额
     *
     * @return BigDecimal 返回成本金额
     */
    BigDecimal sumCostNotAfterSale(long start, long end);

    /**
     * 获取订单数
     *
     * @return Long 返回订单数量
     */
    Long countNotAfterSale(long start, long end);
}


