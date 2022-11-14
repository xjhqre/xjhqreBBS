package com.xjhqre.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.portal.Comment;
import com.xjhqre.common.domain.portal.vo.CommentVO;
import com.xjhqre.common.utils.DateUtils;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.portal.mapper.CommentMapper;
import com.xjhqre.portal.service.CommentService;
import com.xjhqre.portal.service.UserService;

/**
 * <p>
 * CommentServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 10月 12, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    /**
     * 根据条件分页查询评论信息
     * 
     * @param comment
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Comment> findComment(Comment comment, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(comment.getUserId() != null, Comment::getUserId, comment.getUserId())
            .eq(comment.getArticleId() != null, Comment::getArticleId, comment.getArticleId())
            .eq(comment.getParentId() != null, Comment::getParentId, comment.getParentId())
            .eq(comment.getStatus() != null, Comment::getStatus, comment.getStatus())
            .orderByDesc(Comment::getCreateTime);
        return this.commentMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    /**
     * 查询评论详情
     * 
     * @param commentId
     * @return
     */
    @Override
    public Comment selectCommentById(Long commentId) {
        return this.commentMapper.selectById(commentId);
    }

    /**
     * 添加评论
     * 
     * @param comment
     */
    @Override
    public void addComment(Comment comment) {
        comment.setUserId(SecurityUtils.getUserId());
        comment.setCommentCount(0);
        comment.setThumbCount(0);
        comment.setStatus(0);
        comment.setDelFlag("0");
        comment.setCreateBy(SecurityUtils.getUsername());
        comment.setCreateTime(DateUtils.getNowDate());
        comment.setSort(5);
        this.commentMapper.insert(comment);
    }

    /**
     * 查询文章的所有根评论
     * 
     * @param articleId
     * @return
     */
    @Override
    public List<Comment> selectRootComment(Integer articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getParentId, 0).orderByDesc(Comment::getThumbCount);
        return this.commentMapper.selectList(wrapper);
    }

    /**
     * 修改评论
     * 
     * @param comment
     */
    @Override
    public void updateComment(Comment comment) {
        comment.setUpdateBy(SecurityUtils.getUsername());
        comment.setUpdateTime(DateUtils.getNowDate());
        this.commentMapper.updateById(comment);
    }

    /**
     * 删除评论
     * 
     * @param comment
     */
    @Override
    public void deleteComment(Comment comment) {
        this.commentMapper.deleteById(comment);
    }

    /**
     * 根据根评论的ID获取所有子评论，创建时间倒序排序
     *
     * @param list
     *            所有评论
     * @param root
     *            传入的父节点ID
     * @return String
     */
    public List<CommentVO> getChildComment(List<Comment> list, int root) {
        List<CommentVO> returnList = new ArrayList<>();
        for (Comment cur : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (cur.getParentId() == root) {
                this.recursionFn(list, cur, returnList);
                CommentVO commentVO = new CommentVO();
                BeanUtils.copyProperties(cur, commentVO);
                returnList.add(commentVO);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param allCommentList
     *            所有评论
     * @param cur
     *            当前评论
     * @param returnList
     *            返回的子评论
     */
    private void recursionFn(List<Comment> allCommentList, Comment cur, List<CommentVO> returnList) {
        // 得到 cur 的直接子节点列表
        List<CommentVO> childList = this.getChildList(allCommentList, cur);
        returnList.addAll(childList);
        for (Comment tChild : childList) {
            // 判断 tChild 是否还有子节点
            if (!this.getChildList(allCommentList, tChild).isEmpty()) {
                // 递归
                this.recursionFn(allCommentList, tChild, returnList);
            }
        }
    }

    /**
     * 得到当前节点 t 的直接子节点列表
     */
    private List<CommentVO> getChildList(List<Comment> list, Comment cur) {
        List<CommentVO> tList = new ArrayList<>();
        for (Comment child : list) {
            if (child.getParentId().longValue() == cur.getCommentId().longValue()) {
                // 设置回复人姓名
                User user = this.userService.selectUserById(cur.getUserId());
                CommentVO commentVO = new CommentVO();
                BeanUtils.copyProperties(child, commentVO);
                commentVO.setReplyName(user.getUserName());
                tList.add(commentVO);
            }
        }
        return tList;
    }
}
