package com.javablog.smsplatform.webmaster.controller;

import com.javablog.smsplatform.webmaster.pojo.TClientBusiness;
import com.javablog.smsplatform.webmaster.service.ClientBusinessService;
import com.javablog.smsplatform.webmaster.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private ClientBusinessService clientBusinessService;


    @ResponseBody
    @RequestMapping("/sys/client/all")
    public R findAll() {
        List<TClientBusiness> all = clientBusinessService.findAll();
        return R.ok().put("sites", all);
    }


}
