package com.cykj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PayController {
    @RequestMapping("/tohome")
    public void testPage(HttpServletRequest request, @RequestParam("id")String id, HttpServletResponse response){
        System.out.println("id是" + id);

        String params = "";
        String payment = "alipay";
        if (request.getHeader("user-agent").contains("MicroMessenger")){
            System.out.println("微信");
            payment = "wechat";
            params = "?id="+id+"&payment="+payment;
        }else if (request.getHeader("user-agent").contains("AlipayClient")){
            System.out.println("支付宝");
            payment = "alipay";
            params = "?id="+id+"&payment="+payment;
        } else {
            //return "404";
            try {
                response.sendRedirect("http://hutu.natapp1.cc/#/NotFind");
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("支付方式" + payment);
//        return "http://127.0.0.1:8080/#/CenterView" + "?id=1&payment=2";
        try {
            response.sendRedirect("http://hutu.natapp1.cc/#/CenterView" + params);
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @RequestMapping("/saveOrder")
//    public String saveOrder(HttpServletRequest request){
//
//        return "调用了saveOrder";
//    }
}
