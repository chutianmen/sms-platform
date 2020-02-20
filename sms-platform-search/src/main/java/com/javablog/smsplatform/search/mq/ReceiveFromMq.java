package com.javablog.smsplatform.search.mq;

import com.alibaba.fastjson.JSON;
import com.javablog.smsplatform.common.model.Standard_Report;
import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.javablog.smsplatform.common.constants.RabbitMqConsants.TOPIC_SMS_SEND_LOG;

@Component
public class ReceiveFromMq {
    private final static Logger log = LoggerFactory.getLogger(ReceiveFromMq.class);
    @Autowired
    private SearchService searchService;

    /**
     * 消息接受  并发消费
     */
    @RabbitListener(queues = TOPIC_SMS_SEND_LOG, containerFactory = "pointTaskContainerFactory")
    public void receiveSubmitResp(Standard_Submit submit) throws IOException {
        log.info("接收消息submit:{}", submit);
        searchService.add(JSON.toJSONString(submit));
    }

    /**
     * 消息接受  并发消费
     */
    @RabbitListener(queues = "report_update_topic", containerFactory = "pointTaskContainerFactory")
    public void receiveReport(Standard_Report report) throws IOException, InterruptedException {
        log.info("接收消息report:{}", report);
//        Thread.sleep(10000);
        searchService.update(report);
    }
}
