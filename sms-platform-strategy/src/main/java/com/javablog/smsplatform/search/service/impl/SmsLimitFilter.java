package com.javablog.smsplatform.search.service.impl;

import com.javablog.smsplatform.common.constants.CacheConstants;
import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.feign.CacheService;
import com.javablog.smsplatform.search.service.FilterChain;
import com.javablog.smsplatform.search.util.MD5Util;
import com.javablog.smsplatform.search.exception.DataProcessException;
import com.javablog.smsplatform.search.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.javablog.smsplatform.common.constants.StrategyConstants.STRATEGY_ERROR_LIMIT;

/**
 * 	五分钟内，禁止相同客户账户ID向同一手机号码下发相同的短信内容超过3条；
 * 	一小时内，禁止相同客户账户ID向同一手机号码下发相同的短信内容超过5条；
 * 	24小时内，禁止相同客户账户ID向同一手机号码下发相同的短信内容超过10条；
 */
@Service
public class SmsLimitFilter implements FilterChain {
    private final static Logger log = LoggerFactory.getLogger(SmsLimitFilter.class);

    @Value("${smsplatform.sms_send_limit.day}")
    private int dayLimit;
    @Value("${smsplatform.sms_send_limit.hour}")
    private int hourLimit;
    @Value("${smsplatform.sms_send_limit.five_minute}")
    private int fiveMinuteLimit;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private QueueService queueService;

    @Override
    public boolean dealObject(Standard_Submit submit) throws DataProcessException {
        Integer clientId = submit.getClientID();
        String mobile = submit.getDestMobile();
        String smsContent = submit.getMessageContent();
        String keyStr = clientId + mobile + smsContent;
        String five_minute_key = CacheConstants.CACHE_PREFIX_SMS_LIMIT_FIVE_MINUTE + keyStr;
        if (isOutOfRange(MD5Util.md5(five_minute_key), 5 * 60, fiveMinuteLimit)) {
            queueService.sendSubmitToMQ(submit, STRATEGY_ERROR_LIMIT);
            queueService.sendReportToMQ(submit, STRATEGY_ERROR_LIMIT);
            return false;
        }
        String hour_key = CacheConstants.CACHE_PREFIX_SMS_LIMIT_HOUR + keyStr;
        if (isOutOfRange(MD5Util.md5(hour_key), 60 * 60, hourLimit)) {
            queueService.sendSubmitToMQ(submit, STRATEGY_ERROR_LIMIT);
            queueService.sendReportToMQ(submit, STRATEGY_ERROR_LIMIT);
            return false;
        }
        String day_key = CacheConstants.CACHE_PREFIX_SMS_LIMIT_DAY + keyStr;
        if (isOutOfRange(MD5Util.md5(day_key), 60 * 60 * 24, dayLimit)) {
            queueService.sendSubmitToMQ(submit, STRATEGY_ERROR_LIMIT);
            queueService.sendReportToMQ(submit, STRATEGY_ERROR_LIMIT);
            return false;
        }
        return true;
    }

    private boolean isOutOfRange(String key, int time, int limit) {
        Integer count = (Integer) cacheService.getObject(key);
        if (count == null) {
            setExpireAndFirstCount(key, time);
        } else {
            if (count > limit) {
                log.warn("超出发送次数：{}", count);
                return true;
            } else {
                cacheService.incr(key, 1);
            }
        }
        return false;
    }

    private void setExpireAndFirstCount(String key, int limitTime) {
        cacheService.incr(key, 1);
        cacheService.expire(key, limitTime);
    }

}
