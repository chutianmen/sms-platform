package com.javablog.smsplatform.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.javablog.smsplatform.common.model.Standard_Report;
import com.javablog.smsplatform.search.service.SearchService;
import com.javablog.smsplatform.search.util.SearchUtil;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.elasticsearch.common.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("searchService")
public  class SearchServiceImpl implements SearchService {
    private final static Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);
    @Value("${elasticsearch.index.name}")
    private String index; // 集群地址，多个用,隔开
    @Value("${elasticsearch.index.type}")
    private String type; // 集群地址，多个用,隔开
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void createIndex() throws IOException {
        if (!existsIndex(index)) {
            CreateIndexRequest request = new CreateIndexRequest(index);
            buildSetting(request);
            SearchUtil.buildIndexMapping(request, type);
            String ttt = request.index();
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(response.toString());
            System.out.println(response.isAcknowledged());
        } else {
            log.warn("索引：{}，已经存在，不能再创建。", index);
        }
    }

    public boolean existsIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        log.debug("existsIndex: " + exists);
        return exists;
    }

    /**
     * 删除索引
     * @param index 索引名称
     * @throws IOException
     */
    @Override
    public void deleteIndex(String index) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest();
        getIndexRequest.indices(index);
        if (restHighLevelClient.indices().exists(getIndexRequest)) {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            log.info("source:" + deleteIndexRequest.toString());
            restHighLevelClient.indices().delete(deleteIndexRequest);
        }
    }

    //设置分片
    public void buildSetting(CreateIndexRequest request) {
        request.settings(Settings.builder().put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));
    }

    @Override
    public void add(String json) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index, type);
        indexRequest.source(json, XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.debug("add: " + JSON.toJSONString(indexResponse));
    }

    @Override
    public boolean update(Standard_Report report) throws IOException{
        String id =this.termQuery("msgid",report.getMsgId());
        if (id == null){
            log.warn("msgid：{}，没有查到此条纪录。");
        }else {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("reportState", 0);
                map.put("errorCode", report.getErrorCode());
                UpdateRequest request = new UpdateRequest(index, type, id).doc(map);
                UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
                log.debug("update: " + JSON.toJSONString(updateResponse));
            }catch(IOException e){
                log.error(e.getMessage(),e);
                return false;
            }
        }
        return true;
    }

    /**
     * 查询某个字段里含有某个关键词的文档
     * @param fieldName   字段名称
     * @param fieldValue  字段值
     * @return 返回结果列表
     * @throws IOException
     */
    private String  termQuery( String fieldName, String fieldValue) throws IOException {
        List<Map<String,Object>> response =new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.termQuery(fieldName, fieldValue));
        sourceBuilder.query(QueryBuilders.termQuery("msgid", fieldValue));
        searchRequest.source(sourceBuilder);
        log.info("source:" + searchRequest.toString());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:"+hits.totalHits);
        if (hits.totalHits >0 ) {
            SearchHit[] h = hits.getHits();
            String id = h[0].getId();
            return id;
        }
        return null;
    }

    @Override
    public Map<String, Long> statSendStatus(String json) throws IOException {
        RangeQueryBuilder receiveTimeSearch = null;
        TermQueryBuilder clientIDSearch = null;
        HashMap<String, Long> count = new HashMap<String, Long>();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        JSONObject jsonObject = JSONObject.parseObject(json);
        Long startTime = jsonObject.getLong("startTime");
        Long endTime = jsonObject.getLong("endTime");
        Integer clientID = jsonObject.getInteger("clientID");
        if (startTime != null && endTime != null) {
            receiveTimeSearch = QueryBuilders.rangeQuery("sendTime").from(startTime).to(endTime);
        }
        if (startTime != null && endTime == null) {
            receiveTimeSearch = QueryBuilders.rangeQuery("sendTime").gte(startTime);
        }
        if (startTime == null && endTime != null) {
            receiveTimeSearch = QueryBuilders.rangeQuery("sendTime").lte(endTime);
        }
        if (clientID !=0 ){
            clientIDSearch = QueryBuilders.termQuery("clientID", clientID);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (receiveTimeSearch != null) {
            queryBuilder.must(receiveTimeSearch);
        }
        if (clientIDSearch != null) {
            queryBuilder.must(clientIDSearch);
        }
        searchSourceBuilder.query(queryBuilder);
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("byReportState")
                .field("reportState");   //text类型不能用于索引或排序，必须转成keyword类型
        aggregation.subAggregation(AggregationBuilders.count("count").field("reportState"));
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        Aggregations aggregations = searchResponse.getAggregations();
        Terms reportState = aggregations.get("byReportState");
        List<? extends Terms.Bucket> buckets = reportState.getBuckets();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket elasticBucket = buckets.get(i);
            Object key = elasticBucket.getKey();
            long statValue = elasticBucket.getDocCount();
            count.put(key.toString(), statValue);
        }
        log.info("stat result:{}",count);
        return count;
    }

    /**
     * @param searchParams
     * @return
     */
    @Override
    public List<Map> search(String searchParams) throws IOException {
        boolean isShowHighLight = false;
        ArrayList<Map> list = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = SearchUtil.getSearchBuilder(searchParams);
        JSONObject jsonObject = JSONObject.parseObject(searchParams);
        Integer start = jsonObject.getInteger("start");
        Integer rows = jsonObject.getInteger("rows");
        String  keyword = jsonObject.getString("keyword");
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(rows);
        if (StringUtils.isNotBlank(keyword)) {
            String highLightPreTag = jsonObject.getString("highLightPreTag");
            String highLightPostTag = jsonObject.getString("highLightPostTag");

            // 高亮设置
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.requireFieldMatch(false).field("messageContent").preTags(highLightPreTag).postTags(highLightPostTag);
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        //3、发送请求
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        //4、处理响应
        //搜索结果状态信息
        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        Boolean terminatedEarly = searchResponse.isTerminatedEarly();
        boolean timedOut = searchResponse.isTimedOut();

        //分片搜索情况
        int totalShards = searchResponse.getTotalShards();
        int successfulShards = searchResponse.getSuccessfulShards();
        int failedShards = searchResponse.getFailedShards();
        for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
            // failures should be handled here
        }
        //处理搜索命中文档结果
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        float maxScore = hits.getMaxScore();
        log.info("totalHits = " + totalHits);
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            //取_source字段值
            String sourceAsString = hit.getSourceAsString(); //取成json串
            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
            log.info("index:" + index + "  type:" + type + "  id:" + id);
            log.info(sourceAsString);
            if (StringUtils.isNotBlank(keyword)) {
                //取高亮结果
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("messageContent");
                if (highlight != null) {
                    Text[] fragments = highlight.fragments();  //多值的字段会有多个值
                    if (fragments != null) {
                        String fragmentString = fragments[0].string();
                        log.info("title highlight : " + fragmentString);
                        //可用高亮字符串替换上面sourceAsMap中的对应字段返回到上一级调用
                        sourceAsMap.put("messageContent", fragmentString);
                        log.info(JSON.toJSONString(sourceAsMap));
                        list.add(sourceAsMap);
                    }
                }
            }else{
                list.add(sourceAsMap);
            }
        }
        log.info("query result:" + JSON.toJSONString(list));
        return list;
    }


    /*根据查询条件统计记录数*/
    @Override
    public long count(String searchParams) throws IOException {
        SearchSourceBuilder searchSourceBuilder = SearchUtil.getSearchBuilder(searchParams);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        //3、发送请求
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        long total = searchResponse.getHits().getTotalHits();
        return total;
    }
}
