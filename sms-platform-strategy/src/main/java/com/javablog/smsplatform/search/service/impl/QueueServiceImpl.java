package com.javablog.smsplatform.search.service.impl;

import com.javablog.smsplatform.common.model.Standard_Report;
import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javablog.smsplatform.common.constants.RabbitMqConsants;

import java.util.Date;

@Service("queueService")
public class QueueServiceImpl implements QueueService {
    private final static Logger log = LoggerFactory.getLogger(QueueServiceImpl.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;


    @Override
    public void sendSubmitToMQ(Standard_Submit submit,String errorCode) {
        log.info("send mq submit:{}", submit);
        submit.setErrorCode(errorCode);
        submit.setReportState(2);
        submit.setSendTime(new Date());
        rabbitTemplate.convertAndSend(RabbitMqConsants.TOPIC_SMS_SEND_LOG, submit);
    }

    @Override
    public void sendReportToMQ(Standard_Submit submit,String errorCode) {
        log.info("send mq submit:{}", submit);
        //如果为WEB发送就不模拟状态报告，为HTTP就模拟状态报告
        if (submit.getSource()==1){
            Standard_Report report =new Standard_Report();
            report.setClientID(submit.getClientID());
            report.setErrorCode(errorCode);
            report.setMobile(submit.getDestMobile());
            report.setSrcID(submit.getSrcSequenceId());
            report.setState(2);
            rabbitTemplate.convertAndSend(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT, report);
        }

    }
}
