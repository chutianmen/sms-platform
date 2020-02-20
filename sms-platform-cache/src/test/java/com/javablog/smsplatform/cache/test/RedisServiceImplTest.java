package com.javablog.smsplatform.cache.test;

import com.javablog.smsplatform.cache.CacheServiceApplication;
import com.javablog.smsplatform.cache.service.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CacheServiceApplication.class)
public class RedisServiceImplTest {
    private final static Logger log = LoggerFactory.getLogger(RedisServiceImplTest.class);
    private final static String CACHE_PREFIX_PHASE = "PHASE:";
    private final static String CACHE_PREFIX_BLACK = "BLACK:";
    private final static String CACHE_PREFIX_ROUTER = "ROUTER:";
    private final static String CACHE_PREFIX_CUSTOMER_FEE = "CUSTOMER_FEE:";
    private final static String CACHE_PREFIX_DIRTYWORDS = "DIRTYWORDS:";
    private final static String CACHE_PREFIX_CLIENT = "CLIENT:";
    @Autowired
    private CacheService cacheService;

    @Test
    public void initDataTest() {
        //初始化号段数据
        cacheService.set(CACHE_PREFIX_PHASE + "1393927", "14&135");
        cacheService.set(CACHE_PREFIX_PHASE + "1380364", "8&58");
        log.info(cacheService.get(CACHE_PREFIX_PHASE + "1393927"));
        //初始化黑名单数据
        cacheService.set(CACHE_PREFIX_BLACK + "13803640000", "1");
        log.info(cacheService.get(CACHE_PREFIX_BLACK + "13803640000"));
        //初始化费用数据
        cacheService.incr(CACHE_PREFIX_CUSTOMER_FEE + "1001", 10000000);
        log.info("fee:{}", cacheService.getObject(CACHE_PREFIX_CUSTOMER_FEE + "1001"));
        //初始化流控数据
        //初始化路由数据
        cacheService.set(CACHE_PREFIX_ROUTER + "1001", 10);
        log.info("gatewayid:{}", cacheService.getObject(CACHE_PREFIX_ROUTER + "1001"));
        log.info("keys:{}", cacheService.keys(CACHE_PREFIX_PHASE + "*"));
        cacheService.set(CACHE_PREFIX_DIRTYWORDS + "洗钱", "1");
        cacheService.set(CACHE_PREFIX_DIRTYWORDS + "冻结", "1");

        HashMap<String, Object> map = new HashMap<>();
        map.put("ipAddress", "127.0.0.1");
        map.put("pwd", "155659099E18D8983C522D8FC91BB09E");
        map.put("isReturnStatus",1);
        cacheService.hmset(CACHE_PREFIX_CLIENT + "1001", map);
        log.info("map:{}",cacheService.hmget(CACHE_PREFIX_CLIENT + "1001"));
    }

}