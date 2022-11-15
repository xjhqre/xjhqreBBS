package com.xjhqre.portal.mq;

import com.xjhqre.common.config.RabbitMQConfig;
import com.xjhqre.common.utils.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RabbitMQSender
 * </p>
 *
 * @author xjhqre
 * @since 11月 15, 2022
 */
@Component
public class RabbitMQSender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 点赞消息提醒
     *
     * @param articleId
     */
    public void sendThumbMessage(Long articleId) {
        // 通知文章作者
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("collectorId", SecurityUtils.getUserId());
        hashMap.put("articleId", articleId);
        hashMap.put("messageType", "1");
        hashMap.put("type", "点赞");
        // 发送到RabbitMq
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.MESSAGE_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_THUMB_COLLECT, hashMap);
    }

    /**
     * 添加或更新文档
     *
     * @param articleId
     */
    public void sendArticleSaveMessage(String articleId) {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ES_ARTICLE_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_ES_ARTICLE_SAVE, articleId);
    }

    /**
     * 删除文章
     *
     * @param articleId
     */
    public void sendArticleDeleteMessage(String articleId) {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.ES_ARTICLE_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_ES_ARTICLE_DELETE, articleId);
    }

}
