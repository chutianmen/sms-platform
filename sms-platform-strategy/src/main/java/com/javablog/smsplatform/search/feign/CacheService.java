package com.javablog.smsplatform.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "CACHE-SERVICE", fallback = CacheServiceHystrix.class)
public interface CacheService {
    @RequestMapping(value = "/cache/get", method = RequestMethod.GET)
    String get(@RequestParam("key") String key);

    @RequestMapping(value = "/cache/getObject", method = RequestMethod.GET)
    Object getObject(@RequestParam("key") String key);

    @RequestMapping(value = "/cache/decr", method = RequestMethod.GET)
    public Long decr(@RequestParam("key") String key, @RequestParam("value") long value);

    @RequestMapping(value = "/cache/incr", method = RequestMethod.GET)
    public Long incr(@RequestParam("key") String key, @RequestParam("value") long value);

    @RequestMapping(value = "/cache/expire", method = RequestMethod.POST)
    public boolean expire(@RequestParam("key") String key, @RequestParam("expireSecond") Integer expireSecond);

    @RequestMapping(value = "/cache/keys", method = RequestMethod.GET)
    Set<String> keys(@RequestParam("key") String key);

    @RequestMapping(value = "/cache/hmget", method = RequestMethod.GET)
    public Map<Object, Object> hmget(@RequestParam("key") String key);
}