package com.cykj.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface WeChatPayService {

    public ModelAndView createOrder(String state,String openId);
    public int closeOrder(String orderNumber);
    public  ResponseEntity<Map> vxCallBack(HttpServletRequest request);
    public String queryOrder(String outTradeNo);

}
