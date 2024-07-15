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
 * @since 2024-07-04 14:43:14
 */
@Getter
@Setter
@TableName("jc_orders_user")
@ApiModel(value = "JcOrdersUser对象", description = "11")
public class JcOrdersUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("登录密码")
    private String pwd;

    @ApiModelProperty("密码盐")
    private String salt;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("状态 1=>正常 -1=>注销")
    private Integer status;

    @ApiModelProperty("1=>管理员 0=>普通账号")
    private Integer isAdmin;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
