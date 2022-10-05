/*
 * Copyright (c) 2019, CCSSOFT All Rights Reserved.
 */
package com.xjhqre.common.domain.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xjhqre.common.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author xjhqre
 * @since 2022-09-21
 */

@Data
@Schema(name = "User对象", description = "用户表")
@TableName("t_user")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "唯一uid", example = "0")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @NotBlank(message = "用户账号不能为空")
    @Size(max = 30, message = "用户账号长度不能超过30个字符")
    @Schema(name = "用户名")
    private String userName;

    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    @Schema(name = "用户昵称")
    private String nickName;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "性别(0:未设置 1:男 2:女)")
    private String sex;

    @Schema(name = "用户类型：0：超级管理员，1：管理员，2：普通用户 等")
    private String userType;

    @Schema(name = "个人头像")
    private String avatar;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "出生年月日")
    private LocalDate birthday;

    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    @Schema(name = "手机")
    private String mobile;

    @Schema(name = "自我简介最多150字")
    private String summary;

    @Schema(name = "帐号状态（1正常 0停用）")
    private String status;

    @Schema(name = "删除标志（0代表存在 2代表删除）", hidden = true)
    private String delFlag;

    @Schema(name = "最后登录时间", hidden = true)
    private Date loginDate;

    @Schema(name = "最后登录IP", hidden = true)
    private String loginIp;

    @Schema(name = "浏览器", hidden = true)
    private String browser;

    @Schema(name = "操作系统", hidden = true)
    private String os;

    /** 角色对象 */
    private List<Role> roles;

    /** 角色id */
    private List<Long> roleIds;

    public boolean isSuperAdmin() {
        return isSuperAdmin(this.userId);
    }

    public static boolean isSuperAdmin(Long userId) {
        return userId != null && 100L == userId;
    }

}
