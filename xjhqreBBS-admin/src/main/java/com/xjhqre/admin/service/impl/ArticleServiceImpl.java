package com.xjhqre.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.mapper.ArticleMapper;
import com.xjhqre.admin.service.ArticleService;
import com.xjhqre.common.domain.portal.Article;

/**
 * <p>
 * ArticleServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 10月 11, 2022
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    /**
     * 根据条件分页查询文章列表
     * 
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<Article> findArticle(Article article, Integer pageNum, Integer pageSize) {
        article.setStatus(2); // 查询已发布的文章
        return this.articleMapper.findArticle(new Page<>(pageNum, pageSize), article);
    }

    /**
     * 根据文章id查询文章详情
     * 
     * @param articleId
     * @return
     */
    @Override
    public Article selectArticleById(Long articleId) {
        return this.articleMapper.selectArticleById(articleId);
    }

    /**
     * 发布新文章
     * 
     * @param article
     * @return
     */
    @Override
    public int addArticle(Article article) {
        return this.articleMapper.addArticle(article);
    }

    /**
     * 修改文章
     * 
     * @param article
     */
    @Override
    public void updateArticle(Article article) {
        this.articleMapper.updateArticle(article);
    }

    /**
     * 删除文章
     * 
     * @param articleId
     */
    @Override
    public void deleteArticleById(Long articleId) {
        Article article = this.selectArticleById(articleId);
        article.setDelFlag("2"); // 逻辑删除
        this.articleMapper.updateArticle(article);
    }

}
