package com.xjhqre.search.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.search.ESArticleIndex;
import com.xjhqre.search.mapper.SearchMapper;
import com.xjhqre.search.service.ArticleService;
import com.xjhqre.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 高亮字段
     */
    public static final String TITLE = "title"; // 标题
    public static final String SUMMARY = "summary"; // 简介
    public static final String CONTENT = "content"; // 标题

    @Override
    public IPage<Article> searchArticleByES(String keywords, Integer pageNum, Integer pageSize) {


        // 过滤
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(keywords)
                        .field(TITLE, 0.75F)
                        .field(SUMMARY, 0.75F)
                        .field(CONTENT, 0.1F))
                .withPageable(PageRequest.of(pageNum - 1, pageSize))
                .withHighlightFields(
                        new HighlightBuilder.Field(TITLE)
                        , new HighlightBuilder.Field(SUMMARY))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                .build();

        SearchHits<ESArticleIndex> search = this.elasticsearchRestTemplate.search(build, ESArticleIndex.class);

        //得到查询返回的内容
        List<SearchHit<ESArticleIndex>> searchHits = search.getSearchHits();

        //遍历返回的内容进行处理
        for (SearchHit<ESArticleIndex> searchHit : searchHits) {
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setTitle(highlightFields.get(TITLE) == null ? searchHit.getContent().getTitle() : highlightFields.get(TITLE).get(0));
            searchHit.getContent().setSummary(highlightFields.get(SUMMARY) == null ? searchHit.getContent().getSummary() : highlightFields.get(SUMMARY).get(0));
        }

        List<ESArticleIndex> esArticleIndices = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

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
