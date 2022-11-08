package com.xjhqre.common.domain.portal.vo;

import javax.validation.constraints.Size;

import com.xjhqre.common.domain.portal.Collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * CollectVO
 * </p>
 *
 * @author xjhqre
 * @since 10月 20, 2022
 */
@Data
public class CollectVO extends Collect {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "博客简介")
    @Size(max = 200, message = "博客标题简介不能超过200个字符")
    private String summary;

    @ApiModelProperty(value = "作者(用户id)")
    private Long author;

    @ApiModelProperty(value = "作者(用户名)")
    private String authorName;

    @ApiModelProperty(value = "博客浏览数", hidden = true)
    private Integer viewCount;

    @ApiModelProperty(value = "博客点赞数", hidden = true)
    private Integer thumbCount;

    @ApiModelProperty(value = "博客收藏数", hidden = true)
    private Integer collectCount;

    @ApiModelProperty(value = "封面图片地址")
    private String cover;

}
