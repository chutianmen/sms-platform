package com.javablog.smsplatform.search.util;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;
import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

@Component("gatewayQueue")
public class GatewayQueue {
    @Resource
    RabbitAdmin rabbitAdmin;

    private static ConcurrentHashMap<String,Integer> gatwayMap = new ConcurrentHashMap();

    public synchronized  void  createQueue(String queueName){
        Integer flag = gatwayMap.get(queueName);
        if (flag==null){
            Queue queue = new Queue(queueName, true);
            rabbitAdmin.declareQueue(queue);
            gatwayMap.put(queueName,1);
        }
    }

}
