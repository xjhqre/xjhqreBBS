package com.xjhqre.admin.service;

import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjhqre.common.domain.portal.Article;

/**
 * 文章 业务层
 *
 * @author xjhqre
 */
public interface ArticleService extends IService<Article> {
    /**
     * 根据分类id分页查询文章
     *
     * @param sortId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Article> findArticleBySortId(Long sortId, Integer pageNum, Integer pageSize);

    /**
     * 根据标签id分页查询文章
     *
     * @param tagId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Article> findArticleByTagId(Long tagId, Integer pageNum, Integer pageSize);

    /**
     * 根据文章id删除文章
     *
     * @param articleId
     */
    void deleteArticleById(Long articleId);

    /**
     * 统计某篇文章总点赞数
     *
     * @param articleId
     * @return
     */
    Integer countArticleLike(Long articleId);

    /**
     * 获取所有文章的月份
     *
     * @return
     */
    Set<String> getArticleMouth();

    /**
     * 获取所有文章的月份
     *
     * @param month
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Article> findArticleByMonth(String month, Integer pageNum, Integer pageSize);
}
