package com.xunmeng.jccommerce.dto.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页返回
 *
 * @author jinduoxia
 * @since 2023/10/31 上午 11:39
 */
@Data
@Accessors(chain = true)
@ApiModel(value="通用分页查询返回", description="通用分页查询返回")
public class PageVo<T> implements Serializable {

    @ApiModelProperty(value = "当前页")
    private Long current = 1L;

    @ApiModelProperty(value = "每页的条数")
    private Long size = 10L;

    @ApiModelProperty(value = "总条数")
    private Long total = 0L;

    @ApiModelProperty(value = "列表")
    private List<T> records = new ArrayList<>();

}
