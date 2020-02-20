package com.javablog.smsplatform.monitor.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "sms-platform-webmaster", fallback = CacheServiceHystrix.class)
public interface GatewayService {
    @RequestMapping(value="/sys/channel/allid",method = RequestMethod.GET)
    List<Long> findAllIds();
}
