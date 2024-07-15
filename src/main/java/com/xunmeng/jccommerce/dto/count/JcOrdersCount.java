package com.xunmeng.jccommerce.dto.count;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ltm
 * @since 2024/7/4
 */
@Data
@ApiModel(description = "订单统计")
public class JcOrdersCount {

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "订单数量")
    private String phone;

    @ApiModelProperty(value = "订单总金额")
    private Long count;

}
