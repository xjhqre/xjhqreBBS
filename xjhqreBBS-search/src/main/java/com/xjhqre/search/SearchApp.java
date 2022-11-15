package com.xjhqre.search;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.search.ESArticleIndex;
import com.xjhqre.common.exception.ServiceException;
import com.xjhqre.common.utils.BeanUtils;
import com.xjhqre.search.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * SearchApp
 * </p>
 *
 * @author xjhqre
 * @since 11月 10, 2022
 */
@Slf4j

@SpringBootApplication(scanBasePackages = {"com.xjhqre.common.*", "com.xjhqre.search.*"})
public class SearchApp {

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    ArticleService articleService;

    public static void main(String[] args) {
        SpringApplication.run(SearchApp.class, args);
    }

    /**
     * 项目启动时，同步文章信息到elasticsearch
     */
    @PostConstruct
    public void init() {
        log.info("---------- 开始同步文章 ----------");
        this.synchronizeData();
        log.info("---------- 同步文章完成 ----------");
    }

    private void synchronizeData() {

        // 删除已有索引
        IndexOperations indexOperations = this.elasticsearchRestTemplate.indexOps(ESArticleIndex.class);
        boolean delete = indexOperations.delete();
        if (!delete) {
            throw new ServiceException("删除原文章索引失败");
        }
        // 创建索引
        boolean a = indexOperations.create();
        if (a) {
            // 生成映射
            Document mapping = indexOperations.createMapping();
            // 推送映射
            boolean b = indexOperations.putMapping(mapping);
            if (!b) {
                throw new ServiceException("文章索引添加映射失败");
            }
        }

        long page = 1L;
        long pageSize = 10L;
        long size;

        do {
            LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Article::getDelFlag, "0");
            Page<Article> articlePage = this.articleService.page(new Page<>(page, pageSize), wrapper);

            // 取出
            List<ESArticleIndex> esArticleIndices = articlePage.getRecords().stream().map(article -> {
                ESArticleIndex esArticleIndex = new ESArticleIndex();
                BeanUtils.copyBeanProp(esArticleIndex, article);
                return esArticleIndex;
            }).collect(Collectors.toList());

            this.elasticsearchRestTemplate.save(esArticleIndices);
            page++;
            size = articlePage.getTotal();
        } while (size == pageSize);
    }

}
