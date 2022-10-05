package com.xjhqre.common.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户和角色关联 sys_user_role
 * 
 * @author ruoyi
 */
@Data
public class UserRole {

    /** 用户ID */
    @Schema(name = "用户id")
    private Long userId;

    /** 角色ID */
    @Schema(name = "角色id")
    private Long roleId;

}
