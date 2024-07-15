package com.xunmeng.jccommerce.Logic;

import com.xunmeng.jccommerce.base.Result;
import com.xunmeng.jccommerce.dto.count.JcAfterSaleCountVO;
import com.xunmeng.jccommerce.dto.order.JcAmount;
import com.xunmeng.jccommerce.dto.count.JcOrdersSaleTendVO;
import com.xunmeng.jccommerce.mapper.JcOrdersMapper;
import com.xunmeng.jccommerce.service.IJcOrdersService;
import com.xunmeng.jccommerce.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Log4j2
public class CountLogic {

    private final IJcOrdersService jcOrdersService;
    private final JcOrdersMapper jcOrdersMapper;

    /**
     * 订单统计。
     *
     * @param day 截止时间。
     * @return 订单统计数据。
     */
    public Result<JcOrdersSaleTendVO> getSalesCount(Integer day) {
        JcOrdersSaleTendVO jcOrdersSaleTendVO = new JcOrdersSaleTendVO();
        long start = TimeUtil.timestampOffsetDay(1);
        long end = TimeUtil.timestampOffsetDay(1 - day);

        JcAmount saleAmount = jcOrdersService.getSaleAmount(start, end, 0);
        if (saleAmount != null) {
            jcOrdersSaleTendVO.setAllSale(saleAmount.getTotalSale());
        } else {
            jcOrdersSaleTendVO.setAllSale(BigDecimal.ZERO);
        }

        BigDecimal result = jcOrdersService.sumCostNotAfterSale(start, end);
        if (result != null) {
            jcOrdersSaleTendVO.setAllCost(result);
        } else {
            jcOrdersSaleTendVO.setAllCost(BigDecimal.ZERO);
        }

        BigDecimal income = (jcOrdersSaleTendVO.getAllSale() == null ? BigDecimal.ZERO : jcOrdersSaleTendVO.getAllSale())
                .subtract(jcOrdersSaleTendVO.getAllCost() == null ? BigDecimal.ZERO : jcOrdersSaleTendVO.getAllCost());
        jcOrdersSaleTendVO.setInCome(income);

        Long count = jcOrdersService.countNotAfterSale(start, end);
        if (count != null) {
            jcOrdersSaleTendVO.setAllCount(count);
        } else {
            jcOrdersSaleTendVO.setAllCount(0L);
        }

        return Result.newSuccessResponse(jcOrdersSaleTendVO);

    }

    /**
     * 售后统计。
     *
     * @param day 截止时间。
     * @return 售后统计数据。
     */
    public Result<JcAfterSaleCountVO> getAfterSalesCount(Integer day) {
        JcAfterSaleCountVO jcAfterSaleCountVO = new JcAfterSaleCountVO();
        long start = TimeUtil.timestampOffsetDay(1);
        long end = TimeUtil.timestampOffsetDay(1 - day);
        BigDecimal saleAmount = jcOrdersService.getAfterSaleAmount(start, end);
        if (saleAmount == null){
            jcAfterSaleCountVO.setAllSale(BigDecimal.ZERO);
        }else{
            jcAfterSaleCountVO.setAllSale(saleAmount);
        }

        Integer quantity = jcOrdersMapper.sumAfterSaleQuantity(start, end);
        jcAfterSaleCountVO.setAfterSaleQuantity(quantity);

        Integer sumAllQuantity = jcOrdersMapper.sumAllQuantity(start, end);
        jcAfterSaleCountVO.setSaleQuantity(sumAllQuantity);
        if (sumAllQuantity > 0) {
            BigDecimal quantityBig = new BigDecimal(quantity);
            BigDecimal sumAllQuantityBig = new BigDecimal(sumAllQuantity);
            BigDecimal rate = quantityBig.divide(sumAllQuantityBig, 2, RoundingMode.HALF_UP);
            jcAfterSaleCountVO.setAfterSaleRate(rate);
        } else {
            jcAfterSaleCountVO.setAfterSaleRate(BigDecimal.ZERO);
        }

        return Result.newSuccessResponse(jcAfterSaleCountVO);

    }

}
