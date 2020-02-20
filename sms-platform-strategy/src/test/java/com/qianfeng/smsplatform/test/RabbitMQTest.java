package com.qianfeng.smsplatform.test;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.StrategyApplication;
import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StrategyApplication.class)
@WebAppConfiguration
public class RabbitMQTest {
    private final static Logger log = LoggerFactory.getLogger(RabbitMQTest.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Resource
    RabbitAdmin rabbitAdmin;
    @Autowired
    private Channel channel;

    //发送消息
    @Test
    public void testSendMessage() throws IOException {

        for (int i = 0; i < 2; i++) {
            Standard_Submit smsEntity = new Standard_Submit();
            smsEntity.setCityId(1);
            smsEntity.setProvinceId(1);
            smsEntity.setSrcNumber("123");
            smsEntity.setDestMobile("13803640000");
            smsEntity.setOperatorId(1);
            smsEntity.setMessageContent("短信测试！洗钱");
            smsEntity.setSendTime(new Date());
            smsEntity.setClientID(0);
            smsEntity.setSrcSequenceId(i);
//            Queue queue = new Queue("pre_send_sms_topic1111", true);
//            rabbitAdmin.declareQueue(queue);
//            rabbitTemplate.convertAndSend(RabbitMqConsants.TOPIC_PRE_SEND, smsEntity);
//            rabbitTemplate.convertAndSend("pre_send_sms_topic", smsEntity);
            rabbitTemplate.convertAndSend("sms_send_gateway_2",smsEntity);
        }
//        AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive("pre_send_sms");
//        int num = declareOk.getMessageCount();
//        System.out.println("num=========================================" + num);
    }
}
