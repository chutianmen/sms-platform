package com.javablog.smsplatform.search.service;

import com.javablog.smsplatform.common.model.Standard_Report;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchService {
    public void createIndex() throws IOException;
    public void deleteIndex(String index) throws IOException;
    public void add(String json) throws IOException;
    public boolean update(Standard_Report report) throws IOException;
    public Map<String, Long> statSendStatus(String json) throws IOException;
    public long count(String params) throws IOException;
    public List<Map> search(String searchParams) throws IOException;
}

