package com.xjhqre.quartz.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xjhqre.common.domain.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 定时任务调度日志表 sys_job_log
 * 
 * @author ruoyi
 */
@Data
public class JobLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty(name = "日志序号")
    @TableId(value = "job_log_id", type = IdType.AUTO)
    private Long jobLogId;

    /** 任务名称 */
    @ApiModelProperty(name = "任务名称")
    private String jobName;

    /** 任务组名 */
    @ApiModelProperty(name = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
    @ApiModelProperty(name = "调用目标字符串")
    private String invokeTarget;

    /** 日志信息 */
    @ApiModelProperty(name = "日志信息")
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    @ApiModelProperty(name = "执行状态0=正常,1=失败")
    private String status;

    /** 异常信息 */
    @ApiModelProperty(name = "异常信息")
    private String exceptionInfo;

    /** 开始时间 */
    @ApiModelProperty(name = "开始时间")
    private Date startTime;

    /** 停止时间 */
    @ApiModelProperty(name = "停止时间")
    private Date stopTime;
}
