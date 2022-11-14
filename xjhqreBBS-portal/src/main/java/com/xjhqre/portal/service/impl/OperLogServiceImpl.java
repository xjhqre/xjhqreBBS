package com.xjhqre.portal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.OperLog;
import com.xjhqre.portal.mapper.OperLogMapper;
import com.xjhqre.portal.service.OperLogService;

/**
 * 操作日志 服务层处理
 * 
 * @author xjhqre
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {}
