package com.javablog.smsplatform.search.feign;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class CacheServiceHystrix implements CacheService {
    @Override
    public String get(String key) {
        return "";
    }

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public Long decr(String key, long value) {
        return null;
    }

    @Override
    public Long incr(String key, long value) {
        return null;
    }

    @Override
    public boolean expire(String key, Integer expireSecond) {
        return false;
    }

    @Override
    public Set<String> keys(String key) {
        return null;
    }

    @Override
    public Map<Object, Object> hmget(String key) {
        return null;
    }
}
