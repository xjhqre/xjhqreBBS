package com.xjhqre.common.domain.admin;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户和角色关联 sys_user_role
 * 
 * @author xjhqre
 */
@Data
@TableName("t_user_role")
public class UserRole {

    /** 用户ID */
    @ApiModelProperty(name = "用户id")
    private Long userId;

    /** 角色ID */
    @ApiModelProperty(name = "角色id")
    private Long roleId;

}
