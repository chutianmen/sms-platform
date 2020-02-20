package com.javablog.smsplatform.search.service.impl;

import com.javablog.smsplatform.common.constants.CacheConstants;
import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.feign.CacheService;
import com.javablog.smsplatform.search.exception.DataProcessException;
import com.javablog.smsplatform.search.service.FilterChain;
import com.javablog.smsplatform.search.service.QueueService;
import com.javablog.smsplatform.search.util.IKAnalyzerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.javablog.smsplatform.common.constants.StrategyConstants.STRATEGY_ERROR_DIRTYWORDS;

@Service
public class SmsKeyWordFilter implements FilterChain {
    private final static Logger log = LoggerFactory.getLogger(SmsKeyWordFilter.class);
    @Autowired
    private CacheService cacheService;
    @Autowired
    private QueueService queueService;

    @Override
    public boolean dealObject(Standard_Submit submit) throws DataProcessException {
        List<String> words = new ArrayList<>();
        String message = submit.getMessageContent();
        try {
            words = IKAnalyzerUtil.segment(message);
            log.info("分词：{}", words);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        if (words != null && words.size() > 0) {
            for (int i = 0; i < words.size(); i++) {
                String flag = cacheService.get(CacheConstants.CACHE_PREFIX_DIRTYWORDS + words.get(i));
                if (flag != null && flag.equals("1")) {
                    log.warn("查出敏感词：{}", words.get(i));
                    queueService.sendSubmitToMQ(submit,STRATEGY_ERROR_DIRTYWORDS);
                    queueService.sendReportToMQ(submit,STRATEGY_ERROR_DIRTYWORDS);
                    return false;
                }
            }
        }
        return true;
    }

}
