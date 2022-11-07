package com.xjhqre.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : xjhqre
 * @CreateTime : 2022/11/4
 **/
@Configuration
public class RabbitMQConfig {

    // 简单队列，处理单个审核
    public static final String DIRECT_QUEUE_A = "directQueue_A";
    // 处理多个审核
    public static final String DIRECT_QUEUE_B = "directQueue_B";
    // 直连交换机
    public static final String DIRECT_EXCHANGE = "directExchange";
    // 直连路由
    public static final String DIRECT_ROUTING_A = "directRouting_A";
    public static final String DIRECT_ROUTING_B = "directRouting_B";

    // 消息交换机
    public static final String MESSAGE_EXCHANGE = "messageExchange";
    // 点赞收藏队列
    public static final String THUMB_COLLECT = "thumb_collect";
    // 评论和@队列
    public static final String COMMENT = "comment";
    // 粉丝队列
    public static final String FOLLOW = "follow";
    // 路由密钥
    public static final String ROUTING_KEY_THUMB_COLLECT = "thumb_collect_key";
    public static final String ROUTING_KEY_COMMENT = "comment_key";
    public static final String ROUTING_KEY_FOLLOW = "follow_key";

    // 图片处理Direct交换机
    @Bean
    DirectExchange directExchange() {
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.directExchange(DIRECT_EXCHANGE).durable(true).build();
    }

    // 用户消息Direct交换机
    @Bean
    DirectExchange MESSAGE_EXCHANGE() {
        return ExchangeBuilder.directExchange(MESSAGE_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue directQueueA() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // return new Queue("TestDirectQueue",true,true,false);
        // 一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(DIRECT_QUEUE_A, true);
    }

    @Bean
    public Queue directQueueB() {
        return new Queue(DIRECT_QUEUE_B, true);
    }

    @Bean
    public Queue THUMB_COLLECT() {
        return new Queue(THUMB_COLLECT, true);
    }

    @Bean
    public Queue COMMENT() {
        return new Queue(COMMENT, true);
    }

    @Bean
    public Queue FOLLOW() {
        return new Queue(FOLLOW, true);
    }

    // 绑定 将队列和交换机绑定, 并设置用于匹配键
    @Bean
    Binding bindingDirectA() {
        return BindingBuilder.bind(this.directQueueA()).to(this.directExchange()).with(DIRECT_ROUTING_A);
    }

    @Bean
    Binding bindingDirectB() {
        return BindingBuilder.bind(this.directQueueB()).to(this.directExchange()).with(DIRECT_ROUTING_B);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}