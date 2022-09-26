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
 * 管理员表
 * </p>
 *
 * @author xjhqre
 * @since 2022-09-23
 */
@Data
@ApiModel(value = "Admin对象", description = "管理员表")
@TableName("t_admin")
public class Admin implements Serializable {

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

    @ApiModelProperty(value = "性别(1:男2:女)")
    private String gender;

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

    @ApiModelProperty(value = "QQ号")
    private String qqNumber;

}
