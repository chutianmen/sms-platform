package com.javablog.smsplatform.search.mq;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.service.DataFilterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.javablog.smsplatform.common.constants.RabbitMqConsants.TOPIC_PRE_SEND;

@Component
public class ReceiveSmsFromMq {
    private final static Logger log = LoggerFactory.getLogger(ReceiveSmsFromMq.class);
    @Autowired
    private DataFilterManager dataFilterManager;

    /**
     * 消息接受  并发消费
     */
    @RabbitListener(queues = TOPIC_PRE_SEND, containerFactory = "pointTaskContainerFactory")
    public void receive(Standard_Submit submit) throws Exception {
        log.info("接收消息:" + submit);
//        Thread.sleep(10000);
        dataFilterManager.dealObject(submit);
    }

}
