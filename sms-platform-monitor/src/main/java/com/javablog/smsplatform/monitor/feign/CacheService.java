package com.javablog.smsplatform.monitor.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(value = "CACHE-SERVICE", fallback = CacheServiceHystrix.class)
public interface CacheService {
    @RequestMapping(value = "/cache/getObject", method = RequestMethod.GET)
    Object getObject(@RequestParam("key") String key);

    @RequestMapping(value = "/cache/keys", method = RequestMethod.GET)
    Set<String> keys(@RequestParam("pattern") String pattern);
}