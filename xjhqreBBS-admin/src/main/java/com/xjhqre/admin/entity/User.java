/*
 * Copyright (c) 2019, CCSSOFT All Rights Reserved.
 */
package com.xjhqre.admin.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "User对象", description = "用户表")
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一uid", hidden = true)
    @TableId(value = "uid", type = IdType.ASSIGN_ID)
    private Long uid;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String passWord;

    @ApiModelProperty(value = "密码盐", hidden = true)
    private String salt;

    @ApiModelProperty(value = "性别(1:男2:女)", example = "0")
    private Integer gender;

    @ApiModelProperty(value = "个人头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "出生年月日")
    private LocalDate birthday;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "自我简介最多150字")
    private String summary;

    @ApiModelProperty(value = "登录次数", hidden = true)
    private Integer loginCount;

    @ApiModelProperty(value = "最后登录时间", hidden = true)
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "最后登录IP", hidden = true)
    private String lastLoginIp;

    @ApiModelProperty(value = "状态", hidden = true)
    private Integer status;

    @TableField(fill = FieldFill.INSERT) // 插入时填充字段
    @ApiModelProperty(value = "创建时间", hidden = true)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时填充字段
    @ApiModelProperty(value = "更新时间", hidden = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "评论状态 1:正常 0:禁言", hidden = true)
    private Integer commentStatus;

    @ApiModelProperty(value = "ip来源", hidden = true)
    private String ipSource;

    @ApiModelProperty(value = "浏览器", hidden = true)
    private String browser;

    @ApiModelProperty(value = "操作系统", hidden = true)
    private String os;

    @ApiModelProperty(value = "是否开启邮件通知 1:开启 0:关闭", hidden = true)
    private Integer startEmailNotification;

    @ApiModelProperty(value = "用户标签：0：普通用户，1：管理员，2：博主 等", hidden = true)
    private Integer userTag;

}
