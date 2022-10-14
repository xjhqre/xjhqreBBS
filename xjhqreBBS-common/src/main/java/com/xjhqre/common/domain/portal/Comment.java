package com.xjhqre.common.domain.portal;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xjhqre.common.domain.BaseEntity;
import com.xjhqre.common.domain.portal.vo.CommentVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author xjhqre
 * @since 2022-10-12
 */

@ApiModel(value = "Comment对象", description = "评论表")
@TableName("t_comment")
@Data
public class Comment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论id")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    @ApiModelProperty(value = "用户id", example = "0")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "父评论ID", example = "0")
    private Long parentId;

    @ApiModelProperty(value = "文章id", example = "0")
    @NotNull(message = "评论id不能为空")
    private Long articleId;

    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String content;

    @ApiModelProperty(value = "点赞数", hidden = true)
    private Integer thumbCount;

    @ApiModelProperty(value = "评论数", hidden = true)
    private Integer commentCount;

    @ApiModelProperty(value = "状态（1正常 0禁用）", hidden = true)
    private Integer status;

    @ApiModelProperty(value = "排序字段", hidden = true)
    private Integer sort;

    @ApiModelProperty(value = "删除标志（0：正常 2：删除）", hidden = true)
    private String delFlag;

    /** 子评论 */
    private List<CommentVO> children = new ArrayList<>();
}
