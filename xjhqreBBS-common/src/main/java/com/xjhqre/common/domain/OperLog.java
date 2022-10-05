/*
 * Copyright (c) 2019, CCSSOFT All Rights Reserved.
 */
package com.xjhqre.common.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 操作日志记录
 * </p>
 *
 * @author xjhqre
 * @since 2022-10-03
 */

@Schema(name = "OperLog对象", description = "操作日志记录")
@TableName("t_oper_log")
@Data
public class OperLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(name = "日志主键")
    @TableId(value = "oper_id", type = IdType.AUTO)
    private Long operId;

    @Schema(name = "模块标题")
    private String title;

    @Schema(name = "业务类型（0其它 1新增 2修改 3删除）", example = "0")
    private Integer businessType;

    @Schema(name = "方法名称")
    private String method;

    @Schema(name = "请求方式")
    private String requestMethod;

    @Schema(name = "操作人员")
    private String operName;

    @Schema(name = "请求URL")
    private String operUrl;

    @Schema(name = "主机地址")
    private String operIp;

    @Schema(name = "操作地点")
    private String operLocation;

    @Schema(name = "请求参数")
    private String operParam;

    @Schema(name = "返回参数")
    private String jsonResult;

    @Schema(name = "操作状态（1正常 0异常）", example = "0")
    private Integer status;

    @Schema(name = "错误消息")
    private String errorMsg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "操作时间")
    private Date operTime;

}
