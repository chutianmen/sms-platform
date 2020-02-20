package com.javablog.smsplatform.webmaster.service.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "CACHE-SERVICE", fallback = CacheServiceFallback.class)
public interface CacheService {
    @RequestMapping(value = "/cache/get", method = RequestMethod.GET)
    String get(@RequestParam("key") String key);

    @RequestMapping(value = "/cache/getObject", method = RequestMethod.GET)
    Object getObject(@RequestParam("key") String key);

    @RequestMapping(value = "/cache/del", method = RequestMethod.POST)
    boolean del(@RequestParam("key") String key);

    @RequestMapping(value = "/cache/hmset", method = RequestMethod.POST)
    boolean hmset(@RequestParam("key") String key, @RequestBody Map<String,String> map);

    @RequestMapping(value = "/cache/hmget", method = RequestMethod.GET)
    public Map hmget(@RequestParam("key") String key);

    @RequestMapping(value = "/cache/save2Cache", method = RequestMethod.GET)
    void set(@RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("expireSecond") Long expireSecond);

    @RequestMapping(value = "/cache/saveCache", method = RequestMethod.GET)
    void saveCache(@RequestParam("key") String key, @RequestParam("value") String value);

    @RequestMapping(value = "/cache/incr", method = RequestMethod.GET)
    public Long incr(@RequestParam("key") String key, @RequestParam("value") long value);

}
