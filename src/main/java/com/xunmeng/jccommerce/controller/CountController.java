package com.xunmeng.jccommerce.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.xunmeng.jccommerce.Logic.CountLogic;
import com.xunmeng.jccommerce.Logic.OrderLogic;
import com.xunmeng.jccommerce.annotation.AuthToken;
import com.xunmeng.jccommerce.base.Result;
import com.xunmeng.jccommerce.dto.count.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@Api(tags = "03-统计相关接口", value = "统计相关接口")
@RequestMapping("/count")
public class CountController {

    private final CountLogic countLogic;
    private final OrderLogic orderLogic;

    @AuthToken
    @ApiOperation(value = "销售统计")
    @ApiOperationSupport(order = 1)
    @PostMapping("/sale")
    public Result<JcOrdersSaleTendVO> sale(@RequestBody @Validated CountQo req) {
        return countLogic.getSalesCount(req.getDay());
    }

    @AuthToken
    @ApiOperation(value = "售后统计")
    @ApiOperationSupport(order = 2)
    @PostMapping("/afterSale")
    public Result<JcAfterSaleCountVO> afterSale(@RequestBody @Validated CountQo req) {
        return countLogic.getAfterSalesCount(req.getDay());
    }

    @AuthToken
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "销售统计图表")
    @PostMapping("/saleTrend")
    public Result<Map<LocalDate, Map<String, BigDecimal>>> saleTrend(@RequestBody @Validated TrendQo req) {
        return orderLogic.saleTrend(req.getStartDate(), req.getEndDate());
    }

    @AuthToken
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "售后统计图表")
    @PostMapping("/afterSaleTrend")
    public Result<Map<LocalDate, BigDecimal>> afterSaleTrend(@RequestBody @Validated TrendQo req) {
        return orderLogic.afterSaleTrend(req.getStartDate() ,req.getEndDate());
    }

    @AuthToken
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "商品销售周期差值")
    @PostMapping("/rank")
    public Result<SalesDifferenceVO> productRank(@RequestBody @Validated CountQo req) {
        return orderLogic.saleTrendWithDifference(req.getDay());
    }

}
