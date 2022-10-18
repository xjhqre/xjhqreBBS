package com.xjhqre.quartz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.annotation.Log;
import com.xjhqre.common.common.R;
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.enums.BusinessType;
import com.xjhqre.quartz.domain.JobLog;
import com.xjhqre.quartz.service.JobLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 调度日志操作处理
 * 
 * @author ruoyi
 */
@RestController
@Api(value = "任务调度操作日志接口", tags = "任务调度操作日志接口")
@RequestMapping("/monitor/jobLog")
public class JobLogController extends BaseController {
    @Autowired
    private JobLogService jobLogService;

    @ApiOperation(value = "查询定时任务调度日志列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findJobLog/{pageNum}/{pageSize}")
    @PreAuthorize("@ss.hasPermission('monitor:job:list')")
    public R<IPage<JobLog>> findJobLog(JobLog jobLog, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.jobLogService.findJobLog(jobLog, pageNum, pageSize));
    }

    // /**
    // * 导出定时任务调度日志列表
    // */
    // @PreAuthorize("@ss.hasPermission('monitor:job:export')")
    // @Log(title = "任务调度日志", businessType = BusinessType.EXPORT)
    // @PostMapping("/export")
    // public void export(HttpServletResponse response, JobLog jobLog) {
    // List<JobLog> list = this.jobLogService.selectJobLogList(jobLog);
    // ExcelUtil<JobLog> util = new ExcelUtil<JobLog>(JobLog.class);
    // util.exportExcel(response, list, "调度日志");
    // }

    /**
     * 根据调度编号获取详细信息
     */
    @ApiOperation(value = "获取任务日志详情")
    @PreAuthorize("@ss.hasPermission('monitor:job:query')")
    @GetMapping(value = "/{jobLogId}")
    public R<JobLog> getInfo(@PathVariable Long jobLogId) {
        return R.success(this.jobLogService.selectJobLogById(jobLogId));
    }

    /**
     * 删除定时任务调度日志
     */
    @ApiOperation(value = "删除定时任务调度日志")
    @PreAuthorize("@ss.hasPermission('monitor:job:remove')")
    @Log(title = "定时任务调度日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobLogIds}")
    public R<String> remove(@PathVariable Long[] jobLogIds) {
        this.jobLogService.deleteJobLogByIds(jobLogIds);
        return R.success("删除日志成功");
    }

    /**
     * 清空定时任务调度日志
     */
    @ApiOperation(value = "清空定时任务调度日志")
    @PreAuthorize("@ss.hasPermission('monitor:job:remove')")
    @Log(title = "调度日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R<String> clean() {
        this.jobLogService.cleanJobLog();
        return R.success("清空成功");
    }
}
