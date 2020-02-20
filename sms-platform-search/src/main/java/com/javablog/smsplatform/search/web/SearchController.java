package com.javablog.smsplatform.search.web;

import com.javablog.smsplatform.search.service.SearchService;
import com.javablog.smsplatform.search.exception.SearchException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 搜索微服务
 *
 * @author：qianfeng
 * @version: v1.0
 * @date: 2019/5/9 16:34
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    private final static Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private SearchService searchService;

    @ApiOperation(value = "搜索接口")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public List<Map> searchLog(
            @ApiParam(name = "paras", value = "搜索参数", required = false) @RequestParam("paras") String paras) {
        checkParams(paras);
        logger.info("search paras:{}", paras);
        try {
            return searchService.search(paras);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("searchlog方法调用异常:" + e.getMessage());
            throw new SearchException("searchlog方法调用异常" , e.getMessage());
        }
    }

    //返回MAP,APPKEY对应的时长
    @ApiOperation(value = "统计API调用的平均值接口")
    @RequestMapping(value = "/statStatus", method = RequestMethod.POST)
    public Map<String, Long> statSendStatus(
            @ApiParam(name = "paras", value = "统计参数", required = true) @RequestParam("paras") String paras) {
        logger.info("start paras:{}", paras);
        try {
            return searchService.statSendStatus(paras);
        } catch (IOException e) {
            logger.error("statSendStatus方法调用异常:" + e.getMessage());
            throw new SearchException("statSendStatus方法调用异常" , e.getMessage());
        }
    }

    public void checkParams(String paras) {
        if (StringUtils.isBlank(paras)) {
            throw new SearchException("", "参数不能为空");
        }
    }

    @ApiOperation(value = "搜索条数接口")
    @RequestMapping(value = "/searchcount", method = RequestMethod.POST)
    public Long searchLogCount(
            @ApiParam(name = "paras", value = "搜索参数", required = true) @RequestParam("paras") String paras) {
        checkParams(paras);
        logger.info("search paras:{}", paras);
        try {
            return searchService.count(paras);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("searchlog方法调用异常:" + e.getMessage());
            throw new SearchException("searchlog方法调用异常" , e.getMessage());
        }
    }

    @ApiOperation(value = "创建索引接口")
    @RequestMapping(value = "/createindex", method = RequestMethod.POST)
    public boolean createindex() {
        try {
            searchService.createIndex();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("createindex:" + e.getMessage());
        }
        return false;
    }

}
