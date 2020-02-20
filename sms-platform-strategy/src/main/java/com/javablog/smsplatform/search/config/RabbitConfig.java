package com.javablog.smsplatform.search.config;

import com.javablog.smsplatform.common.constants.RabbitMqConsants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * 消费者数量，默认10
     */
    public static final int DEFAULT_CONCURRENT = 1;

    /**
     * 每个消费者获取最大投递数量 默认50
     */
    public static final int DEFAULT_PREFETCH_COUNT = 50;


    //    /**
//     * 注入 Queue
//     *
//     * @return
//     */
    @Bean
    public Queue preSendQueue() {
        return new Queue(RabbitMqConsants.TOPIC_PRE_SEND, true);
    }

    @Bean
    public Queue smsLogQueue() {
        return new Queue(RabbitMqConsants.TOPIC_SMS_SEND_LOG, true);
    }

    @Bean
    public Queue pushSmsReportQueue() {
        return new Queue(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT, true);
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
//        CustomRabbitListenerContainerFactory factory = new CustomRabbitListenerContainerFactory();
        factory.setPrefetchCount(DEFAULT_PREFETCH_COUNT);
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public Channel getChannel(ConnectionFactory connectionFactory) {
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(true);
        return channel;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) throws Exception {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        return rabbitAdmin;
    }

}

