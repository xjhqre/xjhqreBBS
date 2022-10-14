package com.xjhqre.common.domain.portal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xjhqre.common.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author xjhqre
 * @since 2022-10-11
 */

@ApiModel(value = "Article对象", description = "文章表")
@TableName("t_article")
@Data
public class Article extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章id")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    @NotBlank(message = "博客标题不能为空")
    @Size(max = 50, message = "博客标题长度不能超过50个字符")
    @ApiModelProperty(value = "博客标题")
    private String title;

    @ApiModelProperty(value = "博客简介")
    private String summary;

    @ApiModelProperty(value = "博客内容")
    private String content;

    @ApiModelProperty(value = "作者(用户名)")
    private String author;

    @ApiModelProperty(value = "博客浏览数", hidden = true)
    private Integer viewCount;

    @ApiModelProperty(value = "博客点赞数", hidden = true)
    private Integer thumbCount;

    @ApiModelProperty(value = "博客收藏数", hidden = true)
    private Integer collectCount;

    @ApiModelProperty(value = "封面图片地址")
    private String cover;

    @ApiModelProperty(value = "状态（1：正常 0：禁止）", hidden = true)
    private Integer status;

    @ApiModelProperty(value = "是否发布：0：否，1：是")
    private String isPublish;

    @ApiModelProperty(value = "排序字段", hidden = true)
    private Integer sort;

    @ApiModelProperty(value = "是否开启评论(0:否 1:是)")
    private Integer openComment;

    @ApiModelProperty(name = "删除标志（0代表存在 2代表删除）", hidden = true)
    private String delFlag;
}
