package com.javablog.smsplatform.monitor.feign;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CacheServiceHystrix implements CacheService {

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public Set<String> keys(String key) {
        return null;
    }
}
