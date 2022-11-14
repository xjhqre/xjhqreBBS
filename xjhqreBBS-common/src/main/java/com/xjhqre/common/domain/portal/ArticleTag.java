package com.xjhqre.common.domain.portal;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * ArticleTag
 * </p>
 *
 * @author xjhqre
 * @since 11月 12, 2022
 */
@Data
@TableName("t_article_tag")
public class ArticleTag {

    @ApiModelProperty(name = "文章id")
    private Long articleId;

    @ApiModelProperty(name = "标签id")
    private Long tagId;
}
