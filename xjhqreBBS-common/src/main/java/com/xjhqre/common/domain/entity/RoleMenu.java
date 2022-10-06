package com.xjhqre.common.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色和菜单关联 sys_role_menu
 * 
 * @author ruoyi
 */
@Data
public class RoleMenu {
    /** 角色ID */
    @ApiModelProperty(name = "角色id")
    private Long roleId;

    /** 菜单ID */
    @ApiModelProperty(name = "菜单id")
    private Long menuId;
}
