package com.javablog.smsplatform.webmaster.service.api;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 缓存服务的熔断
 */
@Component
public class CacheServiceFallback implements CacheService {
    @Override
    public String get(String key) {
        return "";
    }

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public boolean del(String key) {
        return false;
    }

    @Override
    public boolean hmset(String key, Map<String, String> map) {
        return false;
    }

    @Override
    public Map hmget(String key) {
        return null;
    }

    @Override
    public void set(String key, String value, Long expireSecond) {

    }

    @Override
    public void saveCache(String key, String value) {

    }

    @Override
    public Long incr(String key, long value) {
        return null;
    }

}
