package com.javablog.smsplatform.webmaster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javablog.smsplatform.webmaster.dao.TBlackListMapper;
import com.javablog.smsplatform.webmaster.dto.DataGridResult;
import com.javablog.smsplatform.webmaster.dto.QueryDTO;
import com.javablog.smsplatform.webmaster.pojo.TBlackList;
import com.javablog.smsplatform.webmaster.pojo.TBlackListExample;
import com.javablog.smsplatform.webmaster.service.BlackService;
import com.javablog.smsplatform.webmaster.service.api.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BlackServiceImpl implements BlackService {

    @Autowired
    private TBlackListMapper tBlackListMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public int addBlack(TBlackList tBlackList) {
        cacheService.saveCache("BLACK:"+tBlackList.getMobile(),"1");
        return tBlackListMapper.insertSelective(tBlackList);
    }

    @Override
    public int delBlack(Long id) {
        TBlackList tBlackList = findById(id);
        cacheService.del("BLACK:"+tBlackList.getMobile());
        return tBlackListMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateBlack(TBlackList tBlackList) {
        int i =  tBlackListMapper.updateByPrimaryKey(tBlackList);
        if(i>0){
            cacheService.saveCache("BLACK:"+tBlackList.getMobile(),"1");
        }
        return i;
    }

    @Override
    public TBlackList findById(Long id) {
        return tBlackListMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TBlackList> findAll() {
        return tBlackListMapper.selectByExample(null);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(),queryDTO.getLimit());
        TBlackListExample example = new TBlackListExample();
        String sort = queryDTO.getSort();
        if(!StringUtils.isEmpty(sort)){
            example.setOrderByClause("id");
        }
        List<TBlackList> tBlackLists = tBlackListMapper.selectByExample(example);
        PageInfo<TBlackList> info = new PageInfo<>(tBlackLists);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total,tBlackLists);
        return result;
    }
}
