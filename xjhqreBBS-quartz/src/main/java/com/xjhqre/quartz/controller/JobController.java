package com.xjhqre.quartz.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.annotation.Log;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.Constants;
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.enums.BusinessType;
import com.xjhqre.common.exception.TaskException;
import com.xjhqre.common.utils.StringUtils;
import com.xjhqre.quartz.domain.QuartzJob;
import com.xjhqre.quartz.service.JobService;
import com.xjhqre.quartz.util.CronUtils;
import com.xjhqre.quartz.util.ScheduleUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 调度任务信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@Api(value = "任务调度操作接口", tags = "任务调度操作接口")
@RequestMapping("/monitor/job")
public class JobController extends BaseController {
    @Autowired
    private JobService jobService;

    @ApiOperation(value = "查询定时任务列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findJob/{pageNum}/{pageSize}")
    @PreAuthorize("@ss.hasPermission('monitor:job:list')")
    public R<IPage<QuartzJob>> findJob(QuartzJob quartzJob, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.jobService.findJob(quartzJob, pageNum, pageSize));
    }

    /**
     * 获取定时任务详细信息
     */
    @ApiOperation(value = "获取定时任务详细信息")
    @PreAuthorize("@ss.hasPermission('monitor:job:query')")
    @GetMapping(value = "/{jobId}")
    public R<QuartzJob> getInfo(@PathVariable("jobId") Long jobId) {
        return R.success(this.jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @ApiOperation(value = "新增定时任务")
    @PreAuthorize("@ss.hasPermission('monitor:job:add')")
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody QuartzJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return R.error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return R.error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(),
            new String[] {Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return R.error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(),
            new String[] {Constants.HTTP, Constants.HTTPS})) {
            return R.error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return R.error("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return R.error("新增任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        job.setCreateBy(this.getUsername());
        this.jobService.insertJob(job);
        return R.success("添加定时任务成功");
    }

    /**
     * 修改定时任务
     */
    @ApiOperation(value = "修改定时任务")
    @PreAuthorize("@ss.hasPermission('monitor:job:edit')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@RequestBody QuartzJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return R.error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return R.error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(),
            new String[] {Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return R.error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(),
            new String[] {Constants.HTTP, Constants.HTTPS})) {
            return R.error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return R.error("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return R.error("修改任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        job.setUpdateBy(this.getUsername());
        this.jobService.updateJob(job);
        return R.success("修改定时任务成功");
    }

    /**
     * 定时任务状态修改
     */
    @ApiOperation(value = "定时任务状态修改")
    @PreAuthorize("@ss.hasPermission('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<String> changeStatus(@RequestBody QuartzJob job) throws SchedulerException {
        QuartzJob newJob = this.jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        this.jobService.changeStatus(newJob);
        return R.success("定时任务状态修改成功");
    }

    /**
     * 定时任务立即执行一次
     */
    @ApiOperation(value = "定时任务立即执行一次")
    @PreAuthorize("@ss.hasPermission('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    public R<String> run(@RequestBody QuartzJob job) throws SchedulerException {
        boolean result = this.jobService.run(job);
        return result ? R.success("定时任务执行成功") : R.error("任务不存在或已过期！");
    }

    /**
     * 删除定时任务
     */
    @ApiOperation(value = "删除定时任务")
    @PreAuthorize("@ss.hasPermission('monitor:job:remove')")
    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public R<String> remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException {
        this.jobService.deleteJobByIds(jobIds);
        return R.success("定时任务删除成功");
    }
}
