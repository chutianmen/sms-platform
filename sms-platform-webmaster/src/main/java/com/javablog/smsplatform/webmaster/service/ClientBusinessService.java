package com.javablog.smsplatform.webmaster.service;

import com.javablog.smsplatform.webmaster.dto.DataGridResult;
import com.javablog.smsplatform.webmaster.dto.QueryDTO;
import com.javablog.smsplatform.webmaster.pojo.TClientBusiness;

import java.util.List;

public interface ClientBusinessService {
    public int addClientBusiness(TClientBusiness tClientBusiness);

    public int delClientBusiness(Long id);

    public int updateClientBusiness(TClientBusiness tClientBusiness);

    public TClientBusiness findById(Long id);

    public List<TClientBusiness> findAll();

    public DataGridResult findByPage(QueryDTO queryDTO);
}
