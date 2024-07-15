package com.xunmeng.jccommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmeng.jccommerce.domain.JcOrders;
import com.xunmeng.jccommerce.dto.count.JcOrdersCount;
import com.xunmeng.jccommerce.dto.order.JcAmount;
import com.xunmeng.jccommerce.mapper.JcOrdersMapper;
import com.xunmeng.jccommerce.service.IJcOrdersService;
import com.xunmeng.jccommerce.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author zld
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class JcOrdersServiceImpl extends ServiceImpl<JcOrdersMapper, JcOrders> implements IJcOrdersService {

    @Override
    public List<String> getExistingOrderIds() {
        List<JcOrders> orders =  this.lambdaQuery()
                .select(JcOrders::getId).list();
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream().map(JcOrders::getId).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getPhonesPurchaseMap(Set<String> phones) {
        long startTime = TimeUtil.timestampOffsetDay(-30);
        List<JcOrdersCount> counts = this.baseMapper.getPurchasesNumbers(startTime, phones);
        return counts.stream()
                .collect(Collectors.toMap(JcOrdersCount::getPhone, JcOrdersCount::getCount));
    }

    @Override
    public JcAmount getSaleAmount(Long start, Long end, Integer type) {
        return this.baseMapper.getSaleAmount(start, end,type);
    }

    @Override
    public BigDecimal getAfterSaleAmount(Long start, Long end) {
        return this.baseMapper.getAfterSaleAmount(start, end);
    }

    @Override
    public BigDecimal sumCostNotAfterSale(long start, long end) {
        LambdaQueryWrapper<JcOrders> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(JcOrders::getCost)
                .eq(JcOrders::getIsAfterSale, 0)
                .le(JcOrders::getCreateTime, start)
                .ge(JcOrders::getCreateTime, end);
        List<BigDecimal> costs = this.listObjs(wrapper);
        return costs.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Long countNotAfterSale(long start, long end) {
        LambdaQueryWrapper<JcOrders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JcOrders::getIsAfterSale, 0)
                .le(JcOrders::getCreateTime, start)
                .ge(JcOrders::getCreateTime, end);
        return this.count(wrapper);
    }


}







