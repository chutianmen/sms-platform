package com.javablog.smsplatform.webmaster.service;

import com.javablog.smsplatform.webmaster.dto.DataGridResult;
import com.javablog.smsplatform.webmaster.dto.QueryDTO;
import com.javablog.smsplatform.webmaster.pojo.TDirtyword;

import java.util.List;

public interface DirtywordService {

    public int addDirtyword(TDirtyword tDirtyword);

    public int delDirtyword(Long id);

    public int updateDirtyword(TDirtyword tDirtyword);

    public TDirtyword findById(Long id);

    public List<TDirtyword> findAll();

    public DataGridResult findByPage(QueryDTO queryDTO);

}
