package com.javablog.smsplatform.userinterface.service;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.userinterface.exception.SmsInterfaceException;

import java.util.List;

public interface SmsCheckService {
    public List<Standard_Submit> checkSms(String clientID, String clientAddress, String content, String pwd, String mobile, String srcID)
            throws SmsInterfaceException;
}
