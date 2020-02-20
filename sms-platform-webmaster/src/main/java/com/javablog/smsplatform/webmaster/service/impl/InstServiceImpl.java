package com.javablog.smsplatform.webmaster.service.impl;

import com.javablog.smsplatform.webmaster.service.InstService;
import com.javablog.smsplatform.webmaster.dao.TInstMapper;
import com.javablog.smsplatform.webmaster.pojo.TInst;
import com.javablog.smsplatform.webmaster.pojo.TInstExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstServiceImpl implements InstService {

    @Autowired
    private TInstMapper tInstMapper;

    @Override
    public List<TInst> findProvs() {
        TInstExample tInstExample = new TInstExample();
        TInstExample.Criteria criteria = tInstExample.createCriteria();
        criteria.andParentidEqualTo(1L);
        List<TInst> tInsts = tInstMapper.selectByExample(tInstExample);
        return tInsts;
    }

    @Override
    public List<TInst> findCitys(Long provId) {
        TInstExample tInstExample = new TInstExample();
        TInstExample.Criteria criteria = tInstExample.createCriteria();
        criteria.andParentidEqualTo(provId);
        List<TInst> tInsts = tInstMapper.selectByExample(tInstExample);
        return tInsts;
    }
}
