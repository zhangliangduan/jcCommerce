package com.xunmeng.jccommerce.dto.order;

import com.xunmeng.jccommerce.dto.page.PageQo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("列表公共请求参数")
public class ListQo extends PageQo {

    @ApiModelProperty(value = "Date", example = "2024-07-01", position = 1)
    private Date start;

    @ApiModelProperty(value = "Date", example = "2024-07-01", position = 2)
    private Date end;

    @ApiModelProperty(value = "String", example = "HXB-HXY-007", position = 3)
    private String id;

    @ApiModelProperty(value = "String", example = "4519721202", position = 4)
    private String orderId;

    @ApiModelProperty(value = "String", example = "李耀琴", position = 5)
    private String name;

    @ApiModelProperty(value = "String", example = "15289286395", position = 6)
    private String phone;

    @ApiModelProperty(value = "String", example = "胡小艳", position = 7)
    private String holder;

    @ApiModelProperty(value = "Integer", example = "1", position = 8)
    @Min(value =0, message = "只能为0或1")
    @Max(value =1, message = "只能为0或1")
    private Integer isAfterSale;

}
