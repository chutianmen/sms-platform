package com.javablog.smsplatform.webmaster.service;

import com.javablog.smsplatform.webmaster.dto.DataGridResult;
import com.javablog.smsplatform.webmaster.dto.QueryDTO;
import com.javablog.smsplatform.webmaster.pojo.TBlackList;

import java.util.List;

public interface BlackService {
    public int addBlack(TBlackList tBlackList);

    public int delBlack(Long id);

    public int updateBlack(TBlackList tBlackList);

    public TBlackList findById(Long id);

    public List<TBlackList> findAll();

    public DataGridResult findByPage(QueryDTO queryDTO);
}
