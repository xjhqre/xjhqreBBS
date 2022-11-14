package com.xjhqre.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 根据分类id分页查询文章
     *
     * @param objectPage
     * @param sortId
     * @return
     */
    IPage<Article> findArticleBySortId(@Param("articlePage") Page<Article> objectPage, @Param("sortId") Long sortId);

    /**
     * 根据标签id分页查询文章
     *
     * @param objectPage
     * @param tagId
     * @return
     */
    IPage<Article> findArticleByTagId(@Param("objectPage") Page<Article> objectPage, @Param("tagId") Long tagId);
}
