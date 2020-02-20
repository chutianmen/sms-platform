package com.javablog.smsplatform.search.util;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class SearchUtil {

    public static void buildIndexMapping(CreateIndexRequest request, String type) throws IOException {
        XContentBuilder mappingBuilder = JsonXContent.contentBuilder()
                .startObject()
                .startObject("properties")
                .startObject("msgid")
                .field("type", "keyword")
                .field("index", "true")
                .endObject()

                .startObject("clientID")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("reportState")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("srcNumber")
                .field("type", "keyword")
                .field("index", "true")
                .endObject()

                .startObject("messagePriority")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("srcSequenceId")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("sendTime")
                .field("type", "date")
                .field("index", "true")
                .endObject()

                .startObject("messageContent")
                .field("type", "text")
                .field("analyzer", "ik_max_word") // ik_max_word 这个分词器是ik的，可以去github上搜索安装es的ik分词器插件
                .field("index", "true")
                .endObject()

                .startObject("reportErrorCode")
                .field("type", "keyword")
                .field("index", "true")
                .endObject()

                .startObject("destMobile")
                .field("type", "keyword")
                .field("index", "true")
//                .field("format", "yyyy-MM-dd HH:mm:ss")
                .endObject()

                .startObject("productID")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("gatewayID")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("operatorId")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("provinceId")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("cityId")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .startObject("source")
                .field("type", "long")
                .field("index", "true")
                .endObject()

                .endObject()
                .endObject();
        request.mapping(type, mappingBuilder);
    }

    public static SearchSourceBuilder getSearchBuilder(String searchParams) {
        JSONObject jsonObject = JSONObject.parseObject(searchParams);
        String mobile = jsonObject.getString("mobile");
        String clientID = jsonObject.getString("clientID");
        Long startTime = jsonObject.getLong("startTime");
        Long endTime = jsonObject.getLong("endTime");
        String keyword = jsonObject.getString("keyword");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder mobileSearch = null;
        TermQueryBuilder clientIDSearch = null;
        RangeQueryBuilder receiveTimeSearch = null;
        MatchQueryBuilder keywordSearch = null;
        if (!StringUtils.isEmpty(mobile)) {
            mobileSearch = QueryBuilders.termQuery("destMobile", mobile);
        }
        if (!StringUtils.isEmpty(clientID)) {
            clientIDSearch = QueryBuilders.termQuery("clientID", clientID);
        }
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) ) {
            receiveTimeSearch = QueryBuilders.rangeQuery("sendTime").from(startTime).to(endTime);
        }
        if (!StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
            receiveTimeSearch = QueryBuilders.rangeQuery("sendTime").gte(startTime);
        }
        if (StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            receiveTimeSearch = QueryBuilders.rangeQuery("sendTime").lte(endTime);
        }
        if (!StringUtils.isEmpty(keyword)) {
            keywordSearch = QueryBuilders.matchQuery("messageContent", keyword);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (mobileSearch != null) {
            queryBuilder.must(mobileSearch);
        }
        if (clientIDSearch != null) {
            queryBuilder.must(clientIDSearch);
        }
        if (receiveTimeSearch != null) {
            queryBuilder.must(receiveTimeSearch);
        }
        if (keywordSearch != null) {
            queryBuilder.must(keywordSearch);
        }
        searchSourceBuilder.query(queryBuilder);
        return searchSourceBuilder;
    }
}
