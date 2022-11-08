package com.xjhqre.common.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.sms.Message3;
import com.xjhqre.common.mapper.Message3Mapper;
import com.xjhqre.common.service.Message3Service;

/**
 * <p>
 * Message3ServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 11月 07, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class Message3ServiceImpl extends ServiceImpl<Message3Mapper, Message3> implements Message3Service {}
