package com.xjhqre.sms.listener;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjhqre.common.config.RabbitMQConfig;
import com.xjhqre.common.domain.portal.Article;
import com.xjhqre.common.domain.sms.Message3;
import com.xjhqre.common.service.ArticleService;
import com.xjhqre.common.service.Message3Service;
import com.xjhqre.common.service.UserService;
import com.xjhqre.common.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * MessageListener
 * </p>
 *
 * @author xjhqre
 * @since 11月 04, 2022
 */
@Slf4j
@Component
public class MessageListener {

    @Autowired
    Message3Service message3Service;
    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;

    // 点赞和收藏
    // @RabbitListener(queues = "mogu.email")
    // public void sendMail(Map<String, String> map) {
    // if (map != null) {
    // this.sendMailUtils.sendEmail(map.get("subject"), map.get("receiver"), map.get("text"));
    // }
    // }

    /**
     * 点赞和收藏消息
     * 
     * @param map
     */
    @RabbitListener(queues = RabbitMQConfig.THUMB_COLLECT) // 监听的队列名称
    public void thumbCollectProcess(Map<String, String> map) {
        String type = map.get("type"); // 0点赞1收藏
        Long collectorId = Long.valueOf(map.get("collector_id")); // 点赞收藏人id
        Long articleId = Long.valueOf(map.get("articleId")); // 文章id

        if ("点赞".equals(type)) {
            String messageType = map.get("message_type"); // 0点赞1收藏
            Article article = this.articleService.getById(articleId);
            String collectorName = this.userService.selectUserById(collectorId).getUserName();
            String collectedName = article.getCreateBy();
            Message3 message = new Message3();
            message.setCollectorId(collectorId);
            message.setCollectedId(article.getAuthor());
            message.setCollectorName(collectorName);
            message.setCollectedName(collectedName);
            message.setArticleId(articleId);
            message.setArticleTitle(article.getTitle());
            message.setMessageType(messageType);
            message.setStatus("0"); // 未读
            message.setDelFlag("0");
            message.setCreateBy(collectorName);
            message.setCreateTime(DateUtils.getNowDate());
            this.message3Service.save(message);
        }
        // 取消点赞
        else {
            LambdaQueryWrapper<Message3> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Message3::getCollectorId, collectorId).eq(Message3::getArticleId, articleId);
            Message3 message3 = this.message3Service.getOne(queryWrapper);
            message3.setDelFlag("2");
            this.message3Service.updateById(message3);
        }

    }
}
