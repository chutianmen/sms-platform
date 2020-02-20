package com.javablog.smsplatform.search.test;

import com.alibaba.fastjson.JSON;
import com.javablog.smsplatform.search.service.impl.SearchServiceImpl;
import com.javablog.smsplatform.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.javablog.smsplatform.search.SearchServiceApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SearchServiceApplication.class)
public class SearchServiceTest {
    private final static Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);
    @Autowired
    private SearchService searchService;

    @Test
    public void testCreateIndex() throws IOException {
        searchService.createIndex();
    }

    @Test
    public void testDelIndex() throws IOException {
        searchService.deleteIndex("sms_submit_log");
    }

    @Test
    public void testCount() throws IOException {
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("startTime",0l);
        map.put("endTime",System.currentTimeMillis());
        map.put("clientID",0);
        String json = JSON.toJSONString(map);
        log.info("json:{}",json);
        Map<String, Long> stringLongMap = searchService.statSendStatus(json);
        System.out.println("统计:"+stringLongMap);
    }


    @Test
    public void testAddData() {
        for (int i = 0; i < 500; i++) {
//            GatewayLog gatewayLog = new GatewayLog();
//            gatewayLog.setApiName("taobao.get");
//            gatewayLog.setAppkey("appkey1");
//            gatewayLog.setCreateTime(new Date());
//            gatewayLog.setErrorCode("0000");
//            gatewayLog.setPlatformRepTime(i);
//            gatewayLog.setReceiveTime(new Date());
//            gatewayLog.setRemoteIp("127.0.0.1");
//            gatewayLog.setRequestContent("com.taobao.pop.ware.ic.api.domain.Feature: {cn: \"跟大佬学游戏辅助开发\",fvalue: \"特殊属性value\",key: \"特殊属性key\"}");
//            gatewayLog.setServIP("127.0.0.2");
//            gatewayLog.setVenderId(i);
//            try {
////               Thread.sleep(2000);
////                log.debug(gatewayLog.toString());
////                searchService.add(JSON.toJSONString(gatewayLog));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Test
    public void testAvg() throws IOException {
//        searchService.statAvg(1458332215006l,1658332215119l);
//        searchService.statAvg(1458332215006l, 1659035753905l);
    }

    @Test
    public void testSearch() throws IOException, InterruptedException {
        HashMap<String, Object> searchPara = new HashMap<String, Object>();
        // searchPara.put("clientID","1001");
        searchPara.put("mobile", "13803640000");
        searchPara.put("startTime",0L);
        searchPara.put("endTime", System.currentTimeMillis());
        searchPara.put("keyword", "洗钱");
        searchPara.put("start", 1);
        searchPara.put("rows", 20);
        searchPara.put("highLightPreTag", "<b>");
        searchPara.put("highLightPostTag", "</b>");
        String str = JSON.toJSONString(searchPara);
        List<Map> list = searchService.search(JSON.toJSONString(searchPara));
        System.out.println(list);
    }

}
