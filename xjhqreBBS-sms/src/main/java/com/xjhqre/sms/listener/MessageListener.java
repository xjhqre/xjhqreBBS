package com.xjhqre.sms.listener;

import org.springframework.stereotype.Component;

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

    // 点赞和收藏
    // @RabbitListener(queues = "mogu.email")
    // public void sendMail(Map<String, String> map) {
    // if (map != null) {
    // this.sendMailUtils.sendEmail(map.get("subject"), map.get("receiver"), map.get("text"));
    // }
    // }
}
