package com.xjhqre.portal.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjhqre.common.domain.portal.Comment;

/**
 * <p>
 * CommentMapper
 * </p>
 *
 * @author xjhqre
 * @since 10月 11, 2022
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {}
