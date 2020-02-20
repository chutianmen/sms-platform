package com.javablog.smsplatform.search.service;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.search.exception.DataProcessException;

public interface FilterChain {
    public boolean dealObject(Standard_Submit submit) throws DataProcessException;
}

