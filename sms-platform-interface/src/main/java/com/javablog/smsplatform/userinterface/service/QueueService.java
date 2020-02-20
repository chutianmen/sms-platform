package com.javablog.smsplatform.userinterface.service;

import com.javablog.smsplatform.common.model.Standard_Submit;

import java.util.List;

public interface QueueService {
    public void sendSmsToMQ(List<Standard_Submit> list);
}
