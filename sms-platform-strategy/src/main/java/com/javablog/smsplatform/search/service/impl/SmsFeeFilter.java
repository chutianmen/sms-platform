package com.javablog.smsplatform.search.service.impl;

import com.javablog.smsplatform.common.constants.CacheConstants;
import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.feign.CacheService;
import com.javablog.smsplatform.search.service.FilterChain;
import com.javablog.smsplatform.search.exception.DataProcessException;
import com.javablog.smsplatform.search.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.javablog.smsplatform.common.constants.StrategyConstants.STRATEGY_ERROR_FEE;

@Service
public class SmsFeeFilter implements FilterChain {
    private final static Logger log = LoggerFactory.getLogger(SmsFeeFilter.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private QueueService queueService;

    @Override
    public boolean dealObject(Standard_Submit submit) throws DataProcessException {
        Integer clientId = submit.getClientID();
        String routerKey = CacheConstants.CACHE_PREFIX_ROUTER + clientId;
        Map<Object, Object> map = cacheService.hmget(routerKey);
        Integer price = (Integer) map.get("price");
        String key = CacheConstants.CACHE_PREFIX_CUSTOMER_FEE + clientId;
        log.info("客户扣费KEY：{}", key);
        Integer fee = (Integer) cacheService.getObject(key);
        if (fee != null && fee > 0) {
            //扣费操作
            long totalFee = cacheService.decr(key, price);
            //补全扩展号与网关ID
            String srcNumber = (String) map.get("extendnumber");
            Integer gatewayID = (Integer) map.get("channelid");
            submit.setSrcNumber(srcNumber);
            submit.setGatewayID(gatewayID);
            log.info("客户：{}的账号还有的费用：{}", clientId, totalFee);
            return true;
        } else {
            queueService.sendSubmitToMQ(submit,STRATEGY_ERROR_FEE);
            queueService.sendReportToMQ(submit,STRATEGY_ERROR_FEE);
            return false;
        }
    }

}
