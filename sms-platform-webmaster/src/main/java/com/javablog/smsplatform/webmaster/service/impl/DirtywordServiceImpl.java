package com.javablog.smsplatform.webmaster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javablog.smsplatform.webmaster.dao.TDirtywordMapper;
import com.javablog.smsplatform.webmaster.dto.DataGridResult;
import com.javablog.smsplatform.webmaster.dto.QueryDTO;
import com.javablog.smsplatform.webmaster.pojo.TDirtyword;
import com.javablog.smsplatform.webmaster.pojo.TDirtywordExample;
import com.javablog.smsplatform.webmaster.service.DirtywordService;
import com.javablog.smsplatform.webmaster.service.api.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DirtywordServiceImpl implements DirtywordService {

    @Autowired
    private TDirtywordMapper tDirtywordMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public int addDirtyword(TDirtyword tDirtyword) {
        cacheService.saveCache("DIRTYWORDS:"+tDirtyword.getDirtyword(),"1");
        return tDirtywordMapper.insertSelective(tDirtyword);
    }

    @Override
    public int delDirtyword(Long id) {
        TDirtyword tDirtyword = findById(id);
        cacheService.del("DIRTYWORDS:"+tDirtyword.getDirtyword());
        return tDirtywordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateDirtyword(TDirtyword tDirtyword) {
        int i = tDirtywordMapper.updateByPrimaryKey(tDirtyword);
        if(i>0){
            cacheService.saveCache("DIRTYWORDS:"+tDirtyword.getDirtyword(),"1");
        }
        return i;
    }

    @Override
    public TDirtyword findById(Long id) {
        return tDirtywordMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TDirtyword> findAll() {
        return tDirtywordMapper.selectByExample(null);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(),queryDTO.getLimit());
        TDirtywordExample example = new TDirtywordExample();
        String sort = queryDTO.getSort();
        if(!StringUtils.isEmpty(sort)){
            example.setOrderByClause("id");
        }
        List<TDirtyword> tDirtywords = tDirtywordMapper.selectByExample(example);
        PageInfo<TDirtyword> info = new PageInfo<>(tDirtywords);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total,tDirtywords);
        return result;
    }


}
