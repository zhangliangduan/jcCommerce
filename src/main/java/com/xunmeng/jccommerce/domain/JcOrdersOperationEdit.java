package com.xunmeng.jccommerce.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ltm
 * @since 2024-07-05 08:59:42
 */
@Getter
@Setter
@TableName("jc_orders_operation_edit")
@ApiModel(value = "JcOrdersOperationEdit对象")
public class JcOrdersOperationEdit implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("操作内容")
    private String operationType;

    @ApiModelProperty("操作人名")
    private String name;

    @ApiModelProperty("下载链接")
    private String url;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
