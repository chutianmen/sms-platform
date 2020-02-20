package com.javablog.smsplatform.userinterface.mq;

import com.javablog.smsplatform.common.model.Standard_Report;
import com.javablog.smsplatform.userinterface.service.PushStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

import static com.javablog.smsplatform.common.constants.RabbitMqConsants.TOPIC_PUSH_SMS_REPORT;

@Component
public class ReceiveFromMq {
    private final static Logger log = LoggerFactory.getLogger(ReceiveFromMq.class);
    @Autowired
    private PushStatusService pushStatusService;

    /**
     * 消息接受  并发消费
     *
     */
    @RabbitListener(queues = TOPIC_PUSH_SMS_REPORT, containerFactory = "pointTaskContainerFactory")
    public void receive(Standard_Report report) throws IOException {
        log.info("接收消息:{}" ,report);
        pushStatusService.sendStatus(report);
    }
}
