package com.javablog.smsplatform.monitor.feign;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatewayServiceHystrix implements GatewayService {
    @Override
    public List<Long> findAllIds() {
        return null;
    }
}
