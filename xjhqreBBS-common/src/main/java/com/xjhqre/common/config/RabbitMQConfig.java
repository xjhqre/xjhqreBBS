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

    // 队列
    public static final String DIRECT_QUEUE_A = "directQueue_A"; // 单个图片审核
    public static final String DIRECT_QUEUE_B = "directQueue_B"; // 批量图片审核
    public static final String THUMB_COLLECT = "thumb_collect"; // 点赞收藏队列
    public static final String COMMENT = "comment"; // 评论和@队列
    public static final String FOLLOW = "follow"; // 粉丝队列

    // 交换机
    public static final String DIRECT_EXCHANGE = "directExchange"; // 图片处理交换机
    public static final String MESSAGE_EXCHANGE = "messageExchange"; // 消息交换机

    // 路由密钥
    public static final String DIRECT_ROUTING_A = "directRouting_A"; // 处理图片
    public static final String DIRECT_ROUTING_B = "directRouting_B";
    public static final String ROUTING_KEY_THUMB_COLLECT = "thumb_collect_key"; // 点赞收藏密钥
    public static final String ROUTING_KEY_COMMENT = "comment_key"; // 评论密钥
    public static final String ROUTING_KEY_FOLLOW = "follow_key"; // 关注密钥

    ///////////////////////////////////////// 交换机声明 ///////////////////////////////////////////

    // 图片处理Direct交换机
    @Bean
    DirectExchange directExchange() {
        // 声明路由交换机，durable:在rabbitmq重启后，交换机还在
        return ExchangeBuilder.directExchange(DIRECT_EXCHANGE).durable(true).build();
    }

    // 用户消息Direct交换机
    @Bean
    DirectExchange messageExchange() {
        return ExchangeBuilder.directExchange(MESSAGE_EXCHANGE).durable(true).build();
    }

    ///////////////////////////////////////// 队列声明 ///////////////////////////////////////////

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
    public Queue thumbCollectQueue() {
        return new Queue(THUMB_COLLECT, true);
    }

    @Bean
    public Queue commentQueue() {
        return new Queue(COMMENT, true);
    }

    @Bean
    public Queue followQueue() {
        return new Queue(FOLLOW, true);
    }

    ///////////////////////////////////////// 绑定队列和交换机 ///////////////////////////////////////////

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
    Binding bindingThumbCollect() {
        return BindingBuilder.bind(this.thumbCollectQueue()).to(this.messageExchange()).with(ROUTING_KEY_THUMB_COLLECT);
    }

    @Bean
    Binding bindingComment() {
        return BindingBuilder.bind(this.commentQueue()).to(this.messageExchange()).with(ROUTING_KEY_COMMENT);
    }

    @Bean
    Binding bindingFollow() {
        return BindingBuilder.bind(this.followQueue()).to(this.messageExchange()).with(ROUTING_KEY_FOLLOW);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}