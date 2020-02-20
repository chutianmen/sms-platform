package com.javablog.smsplatform.userinterface.service.impl;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.userinterface.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.javablog.smsplatform.common.constants.RabbitMqConsants.TOPIC_PRE_SEND;


@Service("queueService")
public class QueueServiceImpl implements QueueService {
    private final static Logger log = LoggerFactory.getLogger(QueueServiceImpl.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void sendSmsToMQ(List<Standard_Submit> list) {
        for (int i = 0; i < list.size(); i++) {
            Standard_Submit submit = list.get(i);
            //1表示是HTTP方式发送
            submit.setSource(1);
            rabbitTemplate.convertAndSend(TOPIC_PRE_SEND, submit);
        }
    }
}
