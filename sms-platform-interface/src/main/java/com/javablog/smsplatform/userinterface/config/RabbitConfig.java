package com.javablog.smsplatform.userinterface.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.javablog.smsplatform.common.constants.RabbitMqConsants.TOPIC_PRE_SEND;
import static com.javablog.smsplatform.common.constants.RabbitMqConsants.TOPIC_PUSH_SMS_REPORT;

@Configuration
public class RabbitConfig {

    /**
     * 消费者数量，默认10
     */
    public static final int DEFAULT_CONCURRENT = 10;

    /**
     * 每个消费者获取最大投递数量 默认50
     */
    public static final int DEFAULT_PREFETCH_COUNT = 50;

    /**
     * 注入 Queue
     *
     * @return
     */
    @Bean
    public Queue preSendQueue() {
        return new Queue(TOPIC_PRE_SEND, true);
    }

    /**
     * 注入 Queue
     *
     * @return
     */
    @Bean
    public Queue pushReportQueue() {
        return new Queue(TOPIC_PUSH_SMS_REPORT, true);
    }

    /**
     * 并发消费配置
     *
     * @param configurer
     * @param connectionFactory
     * @return
     */
    @Bean("pointTaskContainerFactory")
    public SimpleRabbitListenerContainerFactory pointTaskContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(DEFAULT_PREFETCH_COUNT);
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public Channel getChannel(ConnectionFactory connectionFactory) {
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);
        return channel;
    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        //数据转换为json存入消息队列
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        return rabbitTemplate;
//    }

}

