package com.xjhqre.sms.listener;

import com.xjhqre.common.config.RabbitMQConfig;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.search.ESArticleIndex;
import com.xjhqre.common.utils.BeanUtils;
import com.xjhqre.sms.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

/**
 * <p>
 * EsArticleListener
 * </p>
 *
 * @author xjhqre
 * @since 11月 15, 2022
 */
@Slf4j
@Component
public class EsArticleListener {

    @Autowired
    ArticleService articleService;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 监听文章添加或更新
     *
     * @param articleId
     */
    @RabbitListener(queues = RabbitMQConfig.ES_ARTICLE_SAVE) // 监听的队列名称
    public void esArticleSava(String articleId) {
        Article article = this.articleService.getById(articleId);
        ESArticleIndex esArticleIndex = new ESArticleIndex();
        BeanUtils.copyBeanProp(esArticleIndex, article);
        this.elasticsearchRestTemplate.save(esArticleIndex);
    }

    /**
     * 监听文章删除
     *
     * @param articleId
     */
    @RabbitListener(queues = RabbitMQConfig.ES_ARTICLE_DELETE) // 监听的队列名称
    public void esArticleDelete(String articleId) {
        this.elasticsearchRestTemplate.delete(articleId, ESArticleIndex.class);
    }
}
