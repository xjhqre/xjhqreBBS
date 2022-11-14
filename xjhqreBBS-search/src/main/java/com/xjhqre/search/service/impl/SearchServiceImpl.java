package com.xjhqre.search.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.search.ESArticleIndex;
import com.xjhqre.search.mapper.SearchMapper;
import com.xjhqre.search.service.ArticleService;
import com.xjhqre.search.service.SearchService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * SearchServiceImpl
 * </p>
 *
 * @author xjhqre
 * @since 11月 10, 2022
 */
@Service
@DubboService // dubbo 提供者的注解
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SearchServiceImpl extends ServiceImpl<SearchMapper, ESArticleIndex> implements SearchService {

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    ArticleService articleService;

    @Override
    public IPage<Article> searchArticleByES(String keywords, Integer pageNum, Integer pageSize) {

        HighlightBuilder.Field titleField =
            new HighlightBuilder.Field("title").preTags("<span style='color:red'>").postTags("</span>");
        HighlightBuilder.Field summaryField =
            new HighlightBuilder.Field("summary").preTags("<span style='color:red'>").postTags("</span>");

        // 创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withPageable(PageRequest.of(pageNum, pageSize));

        // 过滤
        // QueryStringQueryBuilder queryStrBuilder = new QueryStringQueryBuilder(keywords);
        // queryStrBuilder.field("title", 0.75F).field("summary", 0.75F).field("content", 0.1F);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MultiMatchQueryBuilder multiMatchQueryBuilder =
            QueryBuilders.multiMatchQuery(keywords, "title", "summary", "content");
        multiMatchQueryBuilder.field("title", 0.75F);
        multiMatchQueryBuilder.field("summary", 0.75F);
        multiMatchQueryBuilder.field("content", 0.1F);

        boolQueryBuilder.must(multiMatchQueryBuilder);

        queryBuilder.withQuery(boolQueryBuilder);

        queryBuilder.withHighlightFields(titleField, summaryField);

        if (queryBuilder.build().getQuery() != null) {
            log.info("查询语句：{}", queryBuilder.build().getQuery());
        }

        SearchHits<ESArticleIndex> searchHits =
            this.elasticsearchRestTemplate.search(queryBuilder.build(), ESArticleIndex.class);

        List<ESArticleIndex> esArticleIndices =
            searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        // 因为我没有在 es 里存储文章的点阅读量，所以再去查数据库
        List<Article> articles = esArticleIndices.stream().map(esArticleIndex -> {
            Article article = this.articleService.getById(esArticleIndex.getArticleId());
            article.setTitle(esArticleIndex.getTitle());
            article.setSummary(esArticleIndex.getSummary());
            return article;
        }).collect(Collectors.toList());

        return new Page<Article>(pageNum, pageSize).setRecords(articles).setSize(pageSize).setTotal(articles.size());
    }
}
