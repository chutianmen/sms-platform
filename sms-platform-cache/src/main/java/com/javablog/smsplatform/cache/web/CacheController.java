package com.javablog.smsplatform.cache.web;

import com.javablog.smsplatform.cache.exception.ExceptionDict;
import com.javablog.smsplatform.cache.service.CacheService;
import com.javablog.smsplatform.cache.exception.RedisException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * cache controller 版本 v1
 * <p>
 * 升级原因：存储的 value 从请求体中获取，防止存储的value过大导致传输不到服务端
 *
 * @author：qianfeng
 * @version: v1.0
 * @date: 2019/5/9 16:34
 */
@RestController
@RequestMapping("/cache")
public class CacheController {
    private final static Logger log = LoggerFactory.getLogger(CacheController.class);
    @Autowired
    private CacheService redisService;

    @ApiOperation(value = "存字符串类型的数据到cache")
    @RequestMapping(value = "/save2Cache", method = RequestMethod.GET)
    public void save2Cache(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key,
            @ApiParam(name = "value", value = "cache value", required = true) @RequestParam("value") String value,
            @ApiParam(name = "expireSecond", value = "key过期时间:秒,-1为永不过期", required = true) @RequestParam("expireSecond") Integer expireSecond) {
        checkParams(key);
        log.info("key:{},value:{},expireSecond:{}", key, value, expireSecond);
        redisService.set(key, value, expireSecond);
    }

    @ApiOperation(value = "存字符串类型的数据到cache")
    @RequestMapping(value = "/saveCache", method = RequestMethod.GET)
    public void saveCache(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key,
            @ApiParam(name = "value", value = "cache value", required = true) @RequestParam("value") String value) {
        checkParams(key);
        log.info("key:{},value:{},expireSecond:{}", key, value);
        redisService.set(key, value);
    }

    @ApiOperation(value = "取缓存中的值")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key) {
        checkParams(key);
        log.info("key:{}", key);
        return redisService.get(key);
    }

    @ApiOperation(value = "取缓存中的值")
    @RequestMapping(value = "/getObject", method = RequestMethod.GET)
    public Object getObject(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key) {
        checkParams(key);
        log.info("key:{}", key);
        return redisService.getObject(key);
    }

    @ApiOperation(value = "从redis删除字符串类型的数据")
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public boolean del(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key) {
        log.info("key:{},value:{}", key);
        checkParams(key);
        redisService.del(key);
        return true;
    }

    @ApiOperation(value = "存MAP类型的数据到cache")
    @RequestMapping(value = "/hmset", method = RequestMethod.POST)
    public boolean hmset(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key,
            @ApiParam(name = "map", value = "map", required = true) @RequestBody Map<String,Object> map) {
        checkParams(key);
        log.info("key:{},map:{}", key, map);
        return redisService.hmset(key, map);
    }

    @ApiOperation(value = "返回哈希表 key 中的MAP值")
    @RequestMapping(value = "/hmget", method = RequestMethod.GET)
    public Map hmget(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key) {
        checkParams(key);
        log.info("key:{},", key);
        return redisService.hmget(key);
    }

//    @ApiOperation(value = "返回哈希表 key 中的MAP中的值")
//    @RequestMapping(value = "/hget", method = RequestMethod.POST)
//    public String  hgetall(
//            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key,
//            @ApiParam(name = "field", value = "field key", required = true) @RequestParam("field") String field){
//        checkParams(key);
//        log.info("key:{},", key);
//        return redisService.hget(key,field);
//    }

    @ApiOperation(value = "传入一个key，通过redis取得自增id值")
    @RequestMapping(value = "/incr", method = RequestMethod.GET)
    public Long incr(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key,
            @ApiParam(name = "value", value = "incr value", required = true) @RequestParam("value") long value) {
        checkParams(key);
        log.info("key:{},", key);
        return redisService.incr(key, value);
    }

    @ApiOperation(value = "传入一个key，通过redis取得自减id值")
    @RequestMapping(value = "/decr", method = RequestMethod.GET)
    public Long decr(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key,
            @ApiParam(name = "value", value = "decr value", required = true) @RequestParam("value") long value) {
        checkParams(key);
        log.info("key:{},", key);
        return redisService.decr(key, value);
    }

    @ApiOperation(value = "传设置过期时间")
    @RequestMapping(value = "/expire", method = RequestMethod.GET)
    public boolean expire(
            @ApiParam(name = "key", value = "cache key", required = true) @RequestParam("key") String key,
            @ApiParam(name = "expireSecond", value = "key过期时间:秒", required = true) @RequestParam("expireSecond") Integer expireSecond) {
        checkParams(key);
        log.info("key:{},", key);
        return redisService.expire(key, expireSecond);
    }

    @ApiOperation(value = "取前缀为某个值的KEYS集合")
    @RequestMapping(value = "/keys", method = RequestMethod.GET)
    public Set<String> keys(
            @ApiParam(name = "pattern", value = "pattern", required = true) @RequestParam("pattern") String pattern) {
        checkParams(pattern);
        log.info("key:{}", pattern);
        return redisService.keys(pattern);
    }

    public void checkParams(String key) {
        if (StringUtils.isBlank(key)) {
            throw new RedisException(ExceptionDict.paramError, "参数不能为空");
        }
    }

}
