package com.javablog.smsplatform.search.service;

import com.javablog.smsplatform.common.model.Standard_Submit;

public interface QueueService {
    public void sendSubmitToMQ(Standard_Submit submit,String errorCode);
    public void sendReportToMQ(Standard_Submit submit,String errorCode);
}
