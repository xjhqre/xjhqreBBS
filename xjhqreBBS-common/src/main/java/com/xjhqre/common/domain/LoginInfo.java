package com.xjhqre.common.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统访问记录表 t_logininfor
 *
 * @author xjhqre
 */
@Data
public class LoginInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Schema(name = "日志主键")
    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;

    @Schema(name = "用户账号")
    private String userName;

    @Schema(name = "登录状态 1成功 0失败")
    private String status;

    @Schema(name = "登录IP地址")
    private String ipaddr;

    @Schema(name = "登陆地点")
    private String loginLocation;

    @Schema(name = "浏览器")
    private String browser;

    @Schema(name = "操作系统")
    private String os;

    @Schema(name = "提示消息")
    private String msg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "访问时间")
    private Date loginTime;
}
