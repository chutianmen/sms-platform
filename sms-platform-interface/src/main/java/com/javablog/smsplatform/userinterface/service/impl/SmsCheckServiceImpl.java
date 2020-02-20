package com.javablog.smsplatform.userinterface.service.impl;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.common.constants.InterfaceExceptionDict;
import com.javablog.smsplatform.userinterface.exception.SmsInterfaceException;
import com.javablog.smsplatform.userinterface.feign.CacheService;
import com.javablog.smsplatform.userinterface.service.SmsCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("smsCheckService")
public class SmsCheckServiceImpl implements SmsCheckService {
    private final static String CACHE_PREFIX_CLIENT = "CLIENT:";

    @Autowired
    private CacheService cacheService;

    @Override
    public List<Standard_Submit> checkSms(String clientID, String clientAddress, String content, String pwd, String mobile, String srcID) throws SmsInterfaceException {
        List<Standard_Submit> Standard_Submits = new ArrayList<>();
        Integer client;
        Long sourceID;
        try {
            client = Integer.parseInt(clientID);
        } catch (Exception e) {
            throw new SmsInterfaceException(InterfaceExceptionDict.RETURN_STATUS_CLIENTID_ERROR, "没有这个客户。");
        }
        try {
            sourceID = Long.parseLong(srcID);
        } catch (Exception e) {
            throw new SmsInterfaceException(InterfaceExceptionDict.RETURN_STATUS_SRCID_ERROR, "下行编号（srcID）错误。");
        }
        String key = CACHE_PREFIX_CLIENT + clientID;
        Map<Object, Object> map = cacheService.hmget(key);
        if (map == null) {
            throw new SmsInterfaceException(InterfaceExceptionDict.RETURN_STATUS_CLIENTID_ERROR, "没有这个客户。");
        }
        if (!clientAddress.equals(map.get("ipaddress"))) {
            throw new SmsInterfaceException(InterfaceExceptionDict.RETURN_STATUS_IP_ERROR, "IP地址非法。");
        }
//        String strPwd = clientID + pwd;
//        String encodeStr = DigestUtils.md5DigestAsHex(strPwd.getBytes()).toUpperCase();
        if (!pwd.equals(map.get("pwd"))) {
            throw new SmsInterfaceException(InterfaceExceptionDict.RETURN_STATUS_PWD_ERROR, "密码错误。");
        }
        if (content == null || content.length() > 500) {
            throw new SmsInterfaceException(InterfaceExceptionDict.RETURN_STATUS_MESSAGE_ERROR, "短信长度有错误。");
        }
        String[] mob = checkMobile(mobile);
        for (int i = 0; i < mob.length; i++) {
            Standard_Submit submit = new Standard_Submit();
            submit.setClientID(client);
            submit.setSrcSequenceId(sourceID);
            submit.setDestMobile(mob[i]);
            submit.setMessageContent(content);
            submit.setSendTime(new Date());
            Standard_Submits.add(submit);
        }
        return Standard_Submits;
    }

    private String[] checkMobile(String srcMobile) {
        String[] mob = new String[]{};
        if (srcMobile.indexOf(",") <= 0) {
            mob = new String[1];
            mob[0] = srcMobile;
        } else {
            mob = srcMobile.split(",");
        }
        if (mob != null && mob.length > 0) {
            for (String mobile : mob) {
                if (mobile == null || !mobile.matches("\\d{11}")) {
                    throw new SmsInterfaceException(InterfaceExceptionDict.RETURN_STATUS_MOBILE_ERROR, "手机号错误。");
                }
            }
        }
        return mob;
    }
}
