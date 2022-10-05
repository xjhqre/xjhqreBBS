package com.xjhqre.common.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色和菜单关联 sys_role_menu
 * 
 * @author ruoyi
 */
@Data
public class RoleMenu {
    /** 角色ID */
    @Schema(name = "角色id")
    private Long roleId;

    /** 菜单ID */
    @Schema(name = "菜单id")
    private Long menuId;
}
