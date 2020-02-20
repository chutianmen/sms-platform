package com.javablog.smsplatform.monitor.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.javablog.smsplatform.common.constants.RabbitMqConsants;
import com.javablog.smsplatform.monitor.feign.GatewayService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

public class MonitorQueueSizeJob implements SimpleJob {
    private final static Logger log = LoggerFactory.getLogger(MonitorQueueSizeJob.class);

    @Autowired
    private Channel channel;
    @Value("${alert.gatewayQueue}")
    private int limit;
    @Autowired
    private GatewayService gatewayService;
    @Override
    public void execute(final ShardingContext shardingContext) {
        AMQP.Queue.DeclareOk declareOk = null;
        try {
            List<Long> gateways = gatewayService.findAllIds();
            for(int i=0;i<gateways.size();i++) {
                String queueName = RabbitMqConsants.TOPIC_SMS_GATEWAY + i;
                declareOk = channel.queueDeclarePassive(queueName);
                int num = declareOk.getMessageCount();
                log.info("队列{}，长度为：{}", "pre_send_sms", num);
                if (num > limit) {
                    /**
                     * 在这里写报警代码，可以发短信或邮件等。
                     */
                    log.warn("网关发送队列：{}，队列长度为：{}，超过限制！", queueName, num);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }
}
