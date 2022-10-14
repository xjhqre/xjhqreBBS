package com.xjhqre.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.admin.User;
import com.xjhqre.common.domain.portal.Comment;
import com.xjhqre.common.domain.portal.vo.CommentVO;
import com.xjhqre.common.service.UserService;
import com.xjhqre.portal.mapper.CommentMapper;
import com.xjhqre.portal.service.CommentService;

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
public class CommentServiceImpl implements CommentService {

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
        return this.commentMapper.findComment(new Page<>(pageNum, pageSize), comment);
    }

    /**
     * 根据文章id查询评论信息
     * 
     * @param articleId
     * @return
     */
    @Override
    public List<Comment> listCommentByArticleId(Integer articleId) {
        return this.commentMapper.listCommentByArticleId(articleId);
    }

    @Override
    public Comment selectCommentById(Long commentId) {
        return this.commentMapper.selectCommentById(commentId);
    }

    /**
     * 添加评论
     * 
     * @param comment
     */
    @Override
    public void addComment(Comment comment) {
        this.commentMapper.addComment(comment);
    }

    /**
     * 查询文章的所有根评论
     * 
     * @param articleId
     * @return
     */
    @Override
    public List<Comment> selectRootComment(Integer articleId) {
        return this.commentMapper.selectRootComment(articleId);
    }

    /**
     * 修改评论
     * 
     * @param comment
     */
    @Override
    public void updateComment(Comment comment) {
        this.commentMapper.updateComment(comment);
    }

    /**
     * 删除评论
     * 
     * @param comment
     */
    @Override
    public void deleteComment(Comment comment) {
        comment.setDelFlag("2");
        this.commentMapper.updateComment(comment);
    }

    /**
     * 递归列表
     *
     * @param allCommentList
     *            权限列表
     * @param parent
     *            上一级权限
     * @param top
     *            没有父评论的评论
     */
    private void recursionFn(List<Comment> allCommentList, Comment parent, Comment top) {
        // 得到 t 的直接子节点列表
        List<CommentVO> childList = this.getChildList(allCommentList, parent);
        top.setChildren(childList);
        for (Comment tChild : childList) {
            // 判断 tChild 是否还有子节点
            if (this.getChildList(allCommentList, tChild).size() > 0) {
                // 递归
                this.recursionFn(allCommentList, tChild, top);
            }
        }
    }

    /**
     * 得到当前节点 t 的直接子节点列表
     */
    private List<CommentVO> getChildList(List<Comment> list, Comment t) {
        List<CommentVO> tList = new ArrayList<>();
        for (Comment child : list) {
            if (child.getParentId().longValue() == t.getCommentId().longValue()) {
                // 设置回复人姓名
                User user = this.userService.selectUserById(t.getUserId());
                CommentVO commentVO = new CommentVO();
                BeanUtils.copyProperties(child, commentVO);
                commentVO.setReplyName(user.getUserName());
                tList.add(commentVO);
            }
        }
        return tList;
    }
}
