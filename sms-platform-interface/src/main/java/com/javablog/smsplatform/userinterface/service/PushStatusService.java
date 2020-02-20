package com.javablog.smsplatform.userinterface.service;

import com.javablog.smsplatform.common.model.Standard_Report;

public interface PushStatusService {
    public void sendStatus(Standard_Report report);
}
