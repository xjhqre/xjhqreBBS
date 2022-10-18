package com.xjhqre.quartz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xjhqre.common.domain.portal.Article;

/**
 * <p>
 * ArticleMapper
 * </p>
 *
 * @author xjhqre
 * @since 10月 11, 2022
 */
@Mapper
public interface ArticleQuartzMapper {

    /**
     * 根据文章id查询文章详情
     * 
     * @param articleId
     * @return
     */
    Article selectArticleById(@Param("articleId") Long articleId);

    /**
     * 修改文章
     * 
     * @param article
     */
    void updateArticle(Article article);

}
