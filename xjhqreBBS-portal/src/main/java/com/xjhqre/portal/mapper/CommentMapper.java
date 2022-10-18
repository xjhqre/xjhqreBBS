package com.xjhqre.portal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface CommentMapper {

    /**
     * 根据条件分页查询评论信息
     * 
     * @param commentPage
     * @param comment
     */
    IPage<Comment> findComment(@Param("commentPage") Page<Comment> commentPage, @Param("comment") Comment comment);

    /**
     * 查询评论详情
     * 
     * @param commentId
     * @return
     */
    Comment selectCommentById(Long commentId);

    /**
     * 添加评论
     * 
     * @param comment
     */
    void addComment(Comment comment);

    /**
     * 查询某个文章所有的根评论
     * 
     * @param articleId
     * @return
     */
    List<Comment> selectRootComment(@Param("articleId") Integer articleId);

    /**
     * 修改评论
     * 
     * @param comment
     */
    void updateComment(Comment comment);
}
