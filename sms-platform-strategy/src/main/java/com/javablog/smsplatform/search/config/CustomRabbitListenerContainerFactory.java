package com.javablog.smsplatform.search.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public class CustomRabbitListenerContainerFactory extends SimpleRabbitListenerContainerFactory {
    @Override
    protected void initializeContainer(SimpleMessageListenerContainer instance, RabbitListenerEndpoint endpoint) {
        super.initializeContainer(instance, endpoint);
        instance.setAutoDeclare(true);
        instance.setMismatchedQueuesFatal(true);
    }
}
