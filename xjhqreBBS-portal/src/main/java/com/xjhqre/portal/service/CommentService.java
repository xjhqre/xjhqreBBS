package com.xjhqre.portal.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.portal.Comment;

/**
 * 评论 业务层
 *
 * @author xjhqre
 */
public interface CommentService extends IService<Comment> {

    /**
     * 根据条件分页查询评论信息
     *
     * @param comment
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Comment> findComment(Comment comment, Integer pageNum, Integer pageSize);

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
     * 查询文章的所有根评论
     * 
     * @param articleId
     * @return
     */
    List<Comment> selectRootComment(Integer articleId);

    /**
     * 删除评论
     * 
     * @param comment
     */
    void deleteComment(Comment comment);

    /**
     * 修改评论
     * 
     * @param comment
     */
    void updateComment(Comment comment);
}
