package com.javablog.smsplatform.monitor.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.javablog.smsplatform.monitor.feign.CacheService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public class MonitorCustomerFeeJob implements SimpleJob {
    private final static Logger log = LoggerFactory.getLogger(MonitorCustomerFeeJob.class);
    private final static String CACHE_PREFIX_CUSTOMER_FEE = "CUSTOMER_FEE:";
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Channel channel;
    @Value("${alert.fee}")
    private int fee;

    @Override
    public void execute(final ShardingContext shardingContext) {
        Set<String> keys = cacheService.keys(CACHE_PREFIX_CUSTOMER_FEE + "*");
        if (keys.size() > 0) {
            for (String str : keys) {
                log.info("key:{}", str);
                int customerFee = (Integer) cacheService.getObject(str);
                if (fee>customerFee){
                    /**
                     * 在这里写报警代码，可以发短信或邮件等。我们在这里只打日志
                     */
                    log.warn("客户：{}的费用少了100，请及时充值！", keys);
                }
            }
        }
    }
}
