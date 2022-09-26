package com.xjhqre.admin.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("权限模型")
@TableName("t_permission")
public class Permission implements Serializable {

    @ApiModelProperty(value = "唯一uid")
    @TableId(value = "uid", type = IdType.ASSIGN_ID)
    private Long uid;

    @ApiModelProperty(value = "权限名称")
    @NotBlank(message = "权限名称不允许为空")
    private String name;

    @ApiModelProperty(value = "权限路径")
    @NotBlank(message = "权限路径不允许为空")
    private String perUrl;

    @ApiModelProperty(value = "权限类型 1：菜单 2：按钮")
    @NotNull(message = "权限类型不允许为空")
    private Integer type;

}
