package com.xjhqre.portal.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjhqre.common.common.R;
import com.xjhqre.common.constant.ErrorCode;
import com.xjhqre.common.controller.BaseController;
import com.xjhqre.common.domain.portal.Comment;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.SecurityUtils;
import com.xjhqre.portal.service.CommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * CommentController
 * </p>
 *
 * @author xjhqre
 * @since 10月 12, 2022
 */
@Api(value = "评论操作接口", tags = "评论操作接口")
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "分页查询评论列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "正整数，表示查询第几页", required = true, dataType = "int", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "正整数，表示每页几条记录", required = true, dataType = "int",
            example = "20")})
    @GetMapping("findComment/{pageNum}/{pageSize}")
    public R<IPage<Comment>> findComment(Comment comment, @PathVariable("pageNum") Integer pageNum,
        @PathVariable("pageSize") Integer pageSize) {
        return R.success(this.commentService.findComment(comment, pageNum, pageSize));
    }

    @ApiOperation(value = "查询某文章根评论列表")
    @GetMapping("/list/{articleId}")
    public R<List<Comment>> listCommentByArticleId(@PathVariable("articleId") Integer articleId) {
        List<Comment> commentList = this.commentService.selectRootComment(articleId);
        return R.success(commentList);
    }

    @ApiOperation(value = "根据评论编号获取评论详细")
    @GetMapping(value = "/{commentId}")
    public R<Comment> getCommentDetails(@PathVariable Long commentId) {
        if (commentId == null) {
            throw new ServiceException("不允许传入空值");
        }
        return R.success(this.commentService.selectCommentById(commentId));
    }

    @ApiOperation(value = "添加评论")
    @PostMapping(value = "/addComment")
    public R<String> addComment(@RequestBody @Validated Comment comment) {
        if (SecurityUtils.getLoginUser() == null) {
            throw new ServiceException("登陆之后才能评论！！！");
        }
        this.commentService.addComment(comment);
        return R.success("评论成功");
    }

    @ApiOperation(value = "修改评论")
    @PutMapping(value = "/updateComment")
    public R<String> updateComment(@RequestBody @Validated Comment comment) {
        if (!Objects.equals(this.getUserId(), comment.getUserId())) {
            throw new ServiceException("不允许修改别人的评论");
        }
        this.commentService.updateComment(comment);
        return R.success("修改评论成功");
    }

    @ApiOperation(value = "删除评论")
    @DeleteMapping(value = "/deleteComment/{commentId}")
    public R<String> deleteComment(@PathVariable Long commentId) {
        if (commentId == null) {
            throw new ServiceException(ErrorCode.NULL_EXCEPTION, "不允许传入空值");
        }
        Comment comment = this.commentService.selectCommentById(commentId);
        if (!Objects.equals(this.getUserId(), comment.getUserId())) {
            throw new ServiceException("不允许删除别人的评论");
        }
        this.commentService.deleteComment(comment);
        return R.success("删除评论成功");
    }

}
