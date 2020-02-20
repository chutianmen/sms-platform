package com.javablog.smsplatform.search.service.impl;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.feign.CacheService;
import com.javablog.smsplatform.search.service.FilterChain;
import com.javablog.smsplatform.search.util.CheckPhone;
import com.javablog.smsplatform.search.exception.DataProcessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.javablog.smsplatform.common.constants.CacheConstants.CACHE_PREFIX_PHASE;

@Service
public class SmsOpIDFilter implements FilterChain {
    private final static Logger log = LoggerFactory.getLogger(SmsOpIDFilter.class);
    @Autowired
    private CacheService cacheService;

    @Override
    public boolean dealObject(Standard_Submit submit) throws DataProcessException {
        String mobile = submit.getDestMobile();
        //判断手机号是哪一个运营商
        if (CheckPhone.isChinaMobilePhoneNum(mobile)) {
            submit.setOperatorId(1);
        } else if (CheckPhone.isChinaUnicomPhoneNum(mobile)) {
            submit.setOperatorId(2);
        } else {
            submit.setOperatorId(3);
        }
        //从缓存判断手机号是哪个区域
        String key = CACHE_PREFIX_PHASE + mobile.substring(0, 7);
        log.info("phase key:{}", key);
        String area = cacheService.get(key);
        log.info("area value:{}", area);
        if (StringUtils.isNotBlank(area)) {
            String[] areas = area.split("&");
            int provinceId = Integer.parseInt(areas[0]);
            int cityId = Integer.parseInt(areas[1]);
            submit.setProvinceId(provinceId);
            submit.setCityId(cityId);
        } else {
            log.warn("没有找到此上行手机号的区域:{}", mobile);
        }
        return true;
    }
}
