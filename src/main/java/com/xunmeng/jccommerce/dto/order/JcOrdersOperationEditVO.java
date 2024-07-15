package com.xunmeng.jccommerce.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单操作记录")
public class JcOrdersOperationEditVO {

    @ApiModelProperty("操作内容")
    private String operationType;

    @ApiModelProperty("操作人名")
    private String name;

    @ApiModelProperty("下载链接")
    private String url;

    @ApiModelProperty("创建时间")
    private Long createTime;

}
