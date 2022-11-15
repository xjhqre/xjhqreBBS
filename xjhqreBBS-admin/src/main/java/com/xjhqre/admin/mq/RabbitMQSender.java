package com.xjhqre.admin.mq;

import com.xjhqre.common.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * RabbitMQSender 用于发送消息
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
     * 发送图片处理消息
     *
     * @param pictureId
     */
    public void sendPictureProcessMessage(String pictureId) {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING_A,
                pictureId);
    }

    /**
     * 发送图片批量处理消息
     *
     * @param pictureIds
     */
    public void sendPictureBatchProcessMessage(String[] pictureIds) {
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING_B,
                pictureIds);
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
