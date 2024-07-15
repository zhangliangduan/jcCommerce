package com.xunmeng.jccommerce.dto.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 通用分页请求
 *
 * @author ltm
 * @since 2024/7/4
 */
@Data
@ApiModel(value = "通用分页请求", description = "通用分页请求")
public class PageQo implements Serializable {

    @ApiModelProperty(value = "当前页", example = "1", required = true)
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "最小页数为第一页")
    private Long current;

    @ApiModelProperty(value = "每页条数", example = "10", required = true)
    @NotNull(message = "每页条数不能为空")
    private Long size;

}
