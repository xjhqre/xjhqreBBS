/**
 * Copyright (c) 2022, CCSSOFT All Rights Reserved.
 */
package com.xjhqre.admin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * RolePermission
 * </p>
 *
 * @author xjhqre
 * @since 9月 17, 2022
 */
@Data
@ApiModel("角色权限模型")
@TableName("t_role_permission")
public class RolePermission implements Serializable {
    @ApiModelProperty(value = "唯一uid")
    @TableId(value = "uid", type = IdType.ASSIGN_ID)
    private Long uid;

    @ApiModelProperty(value = "角色uid")
    private Long roleUid;

    @ApiModelProperty(value = "权限uid")
    private Long permissionUid;
}
