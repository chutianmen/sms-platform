package com.javablog.smsplatform.search.service.impl;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.feign.CacheService;
import com.javablog.smsplatform.search.exception.DataProcessException;
import com.javablog.smsplatform.search.service.FilterChain;
import com.javablog.smsplatform.search.service.QueueService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.javablog.smsplatform.common.constants.CacheConstants.CACHE_PREFIX_BLACK;

@Service
public class SmsBlackFilter implements FilterChain {
    private final static Logger log = LoggerFactory.getLogger(SmsBlackFilter.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private QueueService queueService;

    @Override
    public boolean dealObject(Standard_Submit submit) throws DataProcessException {
        log.info("submit：{}", submit);
        String mobile = submit.getDestMobile();
        String key = CACHE_PREFIX_BLACK + mobile;
        log.info("黑名单KEY：{}", key);
        String value = cacheService.get(key);
        if (StringUtils.isNotBlank(value)) {
            queueService.sendSubmitToMQ(submit,CACHE_PREFIX_BLACK);
            queueService.sendReportToMQ(submit,CACHE_PREFIX_BLACK);
            return false;
        }
        return true;
    }

}
