package com.javablog.smsplatform.userinterface.web;

import com.javablog.smsplatform.common.model.Standard_Submit;
import com.javablog.smsplatform.userinterface.log.ReceiveLog;
import com.javablog.smsplatform.userinterface.exception.SmsInterfaceException;
import com.javablog.smsplatform.userinterface.service.QueueService;
import com.javablog.smsplatform.userinterface.service.SmsCheckService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/smsInterface", name = "SmsServlet", asyncSupported = false)
public class SmsServlet extends HttpServlet {
    private final static Logger log = LoggerFactory.getLogger(SmsServlet.class);
    @Autowired
    private SmsCheckService smsCheckService;
    @Autowired
    private QueueService queueService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String clientAddress = getIpAddress(request);
        log.debug("获取发送方IP:" + clientAddress);
        String pwd = request.getParameter("pwd");
        log.debug("获取发送方密码:" + pwd);
        String clientID = request.getParameter("clientID");
        String mobile = request.getParameter("mobile");
        String content = request.getParameter("content");
        String srcID = request.getParameter("srcID");
        try {
            ReceiveLog.logger.info("clientID:{},mobile:{},content:{},srcID:{},ip:{},pwd:{}", clientID, mobile, content,
                    srcID, clientAddress, pwd);
            //校验用户发送数据的合法性
            List<Standard_Submit> listSubmit = smsCheckService.checkSms(clientID, clientAddress, content, pwd, mobile, srcID);
            queueService.sendSmsToMQ(listSubmit);
            //数据发送到MQ
            response.getWriter().println("0");
        } catch (SmsInterfaceException ex) {
            log.error(ex.getMsg(), ex);
            response.getWriter().println(ex.getCode());
        }
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        this.doPost(request, response);
    }

    /**
     * @param src String
     * @return String
     */
    private static String encode(String src) {
        if (src == null) {
            return "";
        }
        try {
            src = java.net.URLDecoder.decode(src, "UTF-8");
            return src;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return src;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        try {
            // 获取用户真是的地址
            String Xip = request.getHeader("X-Real-IP");
            // 获取多次代理后的IP字符串值
            String XFor = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
                // 多次反向代理后会有多个IP值，第一个用户真实的IP地址
                int index = XFor.indexOf(",");
                if (index >= 0) {
                    return XFor.substring(0, index);
                } else {
                    return XFor;
                }
            }
            ip = Xip;
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

}
