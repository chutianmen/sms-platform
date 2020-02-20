//package com.qianfeng.smsplatform.search.test;
//
//import com.alibaba.fastjson.JSON;
//import com.qianfeng.smsplatform.search.SearchServiceApplication;
//import com.qianfeng.smsplatform.search.model.ClientSms;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.util.Date;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = SearchServiceApplication.class)
//@WebAppConfiguration
//public class RabbitMQTest {
//    private final static Logger log = LoggerFactory.getLogger(RabbitMQTest.class);
//    @Autowired
//    private AmqpTemplate rabbitTemplate;
//
//    //发送消息
//    @Test
//    public void testSendMessage(){
//        for(int i=0; i < 1; i++){
//            ClientSms gatewayLog =new ClientSms();
//            gatewayLog.setApiName("jingdong.get");
//            gatewayLog.setAppkey("appkey");
//            gatewayLog.setCreateTime(new Date());
//            gatewayLog.setErrorCode("0001");
//            gatewayLog.setPlatformRepTime(i);
//            gatewayLog.setReceiveTime(new Date());
//            gatewayLog.setRemoteIp("127.0.0.1");
//            gatewayLog.setRequestContent("com.taobao.pop.ware.ic.api.domain.Feature: {cn: \"特殊属性中文含义\",fvalue: \"特殊属性value\",key: \"特殊属性key\"}");
//            gatewayLog.setServIP("127.0.0.2");
//            gatewayLog.setVenderId(i);
//            log.debug(gatewayLog.toString());
//            rabbitTemplate.convertAndSend("gw_log",JSON.toJSONString(gatewayLog));
//        }
//    }
//}
