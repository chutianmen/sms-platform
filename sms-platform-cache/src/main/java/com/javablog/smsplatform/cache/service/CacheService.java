package com.javablog.smsplatform.cache.service;

import java.util.Map;
import java.util.Set;

public interface CacheService {
    Boolean set(String key, Object value, int expireTime);

    Boolean set(String key, String value, int expireTime);

    Boolean set(String key, Object value);

    Boolean set(String key, String value);

    String get(String key);

    Object getAndSet(String key, String value);

    Object getObject(String key);

    long size(String key);

    void del(String... keys);

    boolean expire(String key, long seconds);

    long getExpire(String key);

    long incr(String key, long delta);

    long decr(String key, long delta);

    Set<String> keys(String pattern);

    Map<Object, Object> hmget(String key);

    boolean hmset(String key, Map<String, Object> map);
}
