package com.javablog.smsplatform.webmaster.service;

import com.javablog.smsplatform.webmaster.dto.DataGridResult;
import com.javablog.smsplatform.webmaster.dto.QueryDTO;
import com.javablog.smsplatform.webmaster.pojo.TAdminUser;

import java.util.List;

public interface AdminUserService {
    public List<TAdminUser> findAll();

    public TAdminUser findByUsername(String username);

    public DataGridResult findUserByPage(QueryDTO queryDTO);

    public int addAdminUser(TAdminUser tAdminUser);

    public int delAdminUser(Integer id);

    public int updateAdminUser(TAdminUser tAdminUser);

    public TAdminUser findById(Integer id);

}
