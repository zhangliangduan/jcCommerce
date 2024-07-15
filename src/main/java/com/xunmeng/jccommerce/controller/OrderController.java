package com.xunmeng.jccommerce.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.xunmeng.jccommerce.Logic.OrderLogic;
import com.xunmeng.jccommerce.annotation.AuthToken;
import com.xunmeng.jccommerce.base.Result;
import com.xunmeng.jccommerce.dto.*;
import com.xunmeng.jccommerce.dto.order.BatchAfterSaleQo;
import com.xunmeng.jccommerce.dto.order.JcOrdersOperationEditVO;
import com.xunmeng.jccommerce.dto.order.JcOrdersVO;
import com.xunmeng.jccommerce.dto.order.ListQo;
import com.xunmeng.jccommerce.dto.page.PageQo;
import com.xunmeng.jccommerce.dto.page.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Log4j2
@RestController
@RequiredArgsConstructor
@Api(tags = "02-订单相关接口", value = "订单相关接口")
@RequestMapping("/order")
public class OrderController {


    private final OrderLogic orderLogic;

    @AuthToken
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "订单列表")
    @PostMapping("/list")
    public Result<PageVo<JcOrdersVO>> list(@RequestBody @Validated ListQo req) {
        PageVo<JcOrdersVO> vo = orderLogic.list(req);
        return Result.newSuccessResponse(vo);
    }

    @AuthToken
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "删除订单")
    @PostMapping("/delete")
    public Result<String> deleteOrder(@RequestBody @Validated IdQo req) {
        return orderLogic.deleteOrder(req.getId());
    }

    @AuthToken
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "导入EXCEL")
    @PostMapping("/excelInput")
    public Result<String> inputExcel(@RequestParam("file") MultipartFile file) {
        orderLogic.inputExcel(file);
        return Result.newSuccessResponse("success");
    }

    @AuthToken
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "订单售后")
    @PostMapping("/afterSaleBatch")
    public Result<String> afterSale(@RequestBody @Validated BatchAfterSaleQo req) {
        orderLogic.afterSale(req.getIds(), req.getReason(), req.getAmount());
        return Result.newSuccessResponse("success");
    }

    @AuthToken
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "订单取消售后")
    @PostMapping("/cancelAfterSale")
    public Result<String> cancelAfterSale(@RequestBody @Validated IdQo req) {
        orderLogic.cancelAfterSale(req.getId());
        return Result.newSuccessResponse("ok");
    }

    @AuthToken
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "操作记录查询")
    @PostMapping("/edit")
    public PageVo<JcOrdersOperationEditVO> detail(@RequestBody @Validated PageQo req) {
        return orderLogic.getEditPage(req);
    }

}
