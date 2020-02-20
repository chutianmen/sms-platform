package com.javablog.smsplatform.webmaster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javablog.smsplatform.webmaster.dto.DataGridResult;
import com.javablog.smsplatform.webmaster.dto.QueryDTO;
import com.javablog.smsplatform.webmaster.pojo.TAcountRecord;
import com.javablog.smsplatform.webmaster.pojo.TAcountRecordExample;
import com.javablog.smsplatform.webmaster.pojo.TClientBusiness;
import com.javablog.smsplatform.webmaster.service.AcountRecordService;
import com.javablog.smsplatform.webmaster.service.api.CacheService;
import com.javablog.smsplatform.webmaster.dao.TAcountRecordMapper;
import com.javablog.smsplatform.webmaster.dao.TClientBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class AcountRecordImpl implements AcountRecordService {

    @Autowired
    private TAcountRecordMapper tAcountRecordMapper;

    @Autowired
    private TClientBusinessMapper tClientBusinessMapper;

    @Autowired
    private CacheService cacheService;

    //初始化费用数据到缓存
    @Override
    public int addAcount(TAcountRecord tAcountRecord) {
        Integer paidvalue = tAcountRecord.getPaidvalue()*1000;//转为厘
        tAcountRecord.setPaidvalue(paidvalue);
        tAcountRecord.setCreatetime(new Date());
        int i = tAcountRecordMapper.insertSelective(tAcountRecord);
        Integer paidValueStr  = (Integer)cacheService.getObject("CUSTOMER_FEE:" + tAcountRecord.getClientid());
        if(!StringUtils.isEmpty(paidValueStr)){
            paidvalue += paidValueStr;
        }
        cacheService.incr("CUSTOMER_FEE:"+tAcountRecord.getClientid(),paidvalue);
        return i;
    }

    @Override
    public int delAcount(Long id) {
        return tAcountRecordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateAcount(TAcountRecord tAcountRecord) {
        return tAcountRecordMapper.updateByPrimaryKey(tAcountRecord);
    }

    @Override
    public TAcountRecord findById(Long id) {
        return tAcountRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TAcountRecord> findAll() {
        return tAcountRecordMapper.selectByExample(null);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(),queryDTO.getLimit());
        TAcountRecordExample example = new TAcountRecordExample();
        String sort = queryDTO.getSort();
        if(!StringUtils.isEmpty(sort)){
            example.setOrderByClause("id");
        }
        List<TAcountRecord> tAcountRecords = tAcountRecordMapper.selectByExample(example);
        for (TAcountRecord tAcountRecord : tAcountRecords) {
            Long clientid = tAcountRecord.getClientid();
            TClientBusiness tClientBusiness = tClientBusinessMapper.selectByPrimaryKey(clientid);
            String corpname = tClientBusiness.getCorpname();
            tAcountRecord.setCorpname(corpname);
            Integer paidvalue = tAcountRecord.getPaidvalue();
            tAcountRecord.setPaidvalue(paidvalue/1000);
        }
        PageInfo<TAcountRecord> info = new PageInfo<>(tAcountRecords);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total,tAcountRecords);
        return result;
    }


}
