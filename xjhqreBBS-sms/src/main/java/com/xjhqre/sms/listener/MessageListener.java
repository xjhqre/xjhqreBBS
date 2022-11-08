package com.xjhqre.sms.listener;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xjhqre.common.config.RabbitMQConfig;
import com.xjhqre.common.domain.sms.Message3;
import com.xjhqre.common.service.Message3Service;
import com.xjhqre.common.service.UserService;
import com.xjhqre.common.utils.DateUtils;
import com.xjhqre.sms.service.ArticleService;

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

    @RabbitListener(queues = RabbitMQConfig.THUMB_COLLECT) // 监听的队列名称
    public void thumbCollectProcess(Map<String, String> map) {
        Long collectorId = Long.valueOf(map.get("collector_id")); // 点赞收藏人id
        Long collectedId = Long.valueOf(map.get("collected_id")); // 被点赞收藏人id
        String messageType = map.get("message_type"); // 0点赞1收藏
        String collectorName = this.userService.selectUserById(collectorId).getUserName();
        String collectedName = this.userService.selectUserById(collectedId).getUserName();
        Message3 message = new Message3();
        message.setCollectorId(collectorId);
        message.setCollectedId(collectedId);
        message.setCollectorName(collectorName);
        message.setCollectedName(collectedName);
        message.setMessageType(messageType);
        message.setCreateBy(collectorName);
        message.setCreateTime(DateUtils.getNowDate());
        this.message3Service.save(message);
    }
}
