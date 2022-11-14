package com.xjhqre.common.domain.portal;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * ArticleSort
 * </p>
 *
 * @author xjhqre
 * @since 11月 12, 2022
 */
@Data
@TableName("t_article_sort")
public class ArticleSort {

    @ApiModelProperty(name = "文章id")
    private Long articleId;

    @ApiModelProperty(name = "分类id")
    private Long sortId;
}
