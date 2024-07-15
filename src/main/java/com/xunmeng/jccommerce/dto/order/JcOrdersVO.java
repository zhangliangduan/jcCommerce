package com.xunmeng.jccommerce.dto.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@ApiModel("信息返回")
public class JcOrdersVO {

    @ApiModelProperty(value = "佳诚自编订单号", example = "app123456", dataType = "String")
    @ExcelProperty(value = "佳诚自编订单号")
    private String id;

    @ApiModelProperty(value = "得力平台订单号", example = "app123456", dataType = "String")
    @ExcelProperty(value = "得力平台订单号")
    private String orderId;

    @ApiModelProperty(value = "客户姓名", example = "张三", dataType = "String")
    @ExcelProperty(value = "客户姓名")
    private String name;

    @ApiModelProperty(value = "客户电话", example = "13965475551", dataType = "String")
    @ExcelProperty(value = "客户电话")
    private String phone;

    @ApiModelProperty(value = "客户地址", example = "宁夏银川市兴庆区宁夏银川市兴庆区石油城7区1组20楼", dataType = "String")
    @ExcelProperty(value = "客户地址")
    private String address;

    @ApiModelProperty(value = "商品信息", example = "狼爪(Jack Wolfskin)4042441-6000徒步鞋男40.5黑色（双）", dataType = "String")
    @ExcelProperty(value = "商品信息")
    private String information;

    @ApiModelProperty(value = "数量", example = "1", dataType = "Integer")
    @ExcelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "单价", example = "200", dataType = "Double")
    @ExcelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "销售额", example = "200", dataType = "Double")
    @ExcelProperty(value = "销售额")
    private BigDecimal sale;

    @ApiModelProperty(value = "平台备注", example = "已收到换货商品", dataType = "String")
    @ExcelProperty(value = "平台备注")
    private String platformRemarks;

    @ApiModelProperty(value = "成本", example = "200.3", dataType = "Double")
    @ExcelProperty(value = "成本")
    private BigDecimal cost;

    @ApiModelProperty(value = "内部备注", example = "没有退回天马，在安徽", dataType = "String")
    @ExcelProperty(value = "内部备注")
    private String internalRemarks;

    @ApiModelProperty(value = "平台/项目", example = "得力-油田项目", dataType = "String")
    @ExcelProperty(value = "平台/项目")
    private String platform;

    @ApiModelProperty(value = "下单渠道", example = "天马", dataType = "String")
    @ExcelProperty(value = "下单渠道")
    private String channel;

    @ApiModelProperty(value = "下单人", example = "胡小艳", dataType = "String")
    @ExcelProperty(value = "下单人")
    private String holder;

    @ApiModelProperty(value = "售后理由")
    private String afterSaleReason;

    @TableField(value = "30天内购买次数",insertStrategy = FieldStrategy.NEVER,updateStrategy = FieldStrategy.NEVER)
    private Long PurchasesNumber;

    @ApiModelProperty(value = "售后金额", example = "500", dataType = "BigDecimal")
    private BigDecimal afterSaleAmount;

    @ApiModelProperty(value = "时间", example = "1719792000", dataType = "Long")
    private Long createTime;

}
