package com.javablog.smsplatform.search.service.impl;

import com.javablog.smsplatform.common.constants.RabbitMqConsants;
import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.feign.CacheService;
import com.javablog.smsplatform.search.service.FilterChain;
import com.javablog.smsplatform.search.exception.DataProcessException;
import com.javablog.smsplatform.search.service.QueueService;
import com.javablog.smsplatform.search.util.GatewayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.javablog.smsplatform.common.constants.StrategyConstants.STRATEGY_ERROR_ROUTER;

@Service
public class SmsRouterFilter implements FilterChain {
    private final static Logger log = LoggerFactory.getLogger(SmsRouterFilter.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private QueueService queueService;
    @Resource
    private GatewayQueue gatewayQueue;

    @Override
    public boolean dealObject(Standard_Submit submit) throws DataProcessException {
        Integer gatewayID =submit.getGatewayID();
        if (gatewayID != null) {
            String topic = RabbitMqConsants.TOPIC_SMS_GATEWAY + gatewayID;
            log.info("gateway topic:{}",topic);
            gatewayQueue.createQueue(topic);
            rabbitTemplate.convertAndSend(topic,submit);
        }else{
            queueService.sendSubmitToMQ(submit, STRATEGY_ERROR_ROUTER);
            queueService.sendReportToMQ(submit, STRATEGY_ERROR_ROUTER);
            return false;
        }
        return true;
    }
}
