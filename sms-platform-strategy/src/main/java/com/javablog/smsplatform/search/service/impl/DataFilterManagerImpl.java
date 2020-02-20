package com.javablog.smsplatform.search.service.impl;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.service.DataFilterManager;
import com.javablog.smsplatform.search.service.FilterChain;
import com.javablog.smsplatform.search.exception.DataProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("dataFilterManager")
public class DataFilterManagerImpl implements DataFilterManager {
    private final static Logger log = LoggerFactory.getLogger(DataFilterManagerImpl.class);
    @Autowired
    private Map<String, FilterChain> filterChainMap;

    @Value("${smsplatform.filters}")
    private String filters;

    @Override
    public void dealObject(Standard_Submit submit) {
        String[] filterArray = filters.split(",");
        if (submit != null && filterArray != null) {
            try {
                for (int i = 0; i < filterArray.length; i++) {
                    String filterName = filterArray[i];
                    FilterChain filter = filterChainMap.get(filterName);
                    boolean flag = filter.dealObject(submit);
                    if (!flag) break;
                }
            } catch (DataProcessException e) {
                log.debug("过滤错误：" + e.getMessage());
            }
        }
    }
}
