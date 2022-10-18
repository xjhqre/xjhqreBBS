package com.xjhqre.common.domain.portal.dto;

import javax.validation.constraints.NotNull;

import com.xjhqre.common.domain.portal.Article;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * ArticleDTO
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
@Data
public class ArticleDTO extends Article {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类id")
    @NotNull(message = "博客分类不能为空")
    private Long sortId;

    @NotNull(message = "标签集合不能为空")
    @ApiModelProperty(value = "标签id")
    private Long[] tagIds;
}
