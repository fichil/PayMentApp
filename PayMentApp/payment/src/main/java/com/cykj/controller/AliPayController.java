package com.cykj.controller;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.cykj.feign.OrderFeignClient;
import com.cykj.mqConsumer.AlipaySendCallBack;
import com.cykj.service.AlipayService;
import com.cykj.utils.AliPay;
import com.cykj.utils.TimeUtil;
import com.cykj.vo.CallBackVO;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/aliPay")
@CrossOrigin
public class AliPayController {
    @Autowired
    AlipayService alipayService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    OrderFeignClient orderFeignClient;

    @RequestMapping("/createOrder")
    public String createOrder(String merchantId,String total){
        String pageRedirectionData = alipayService.createOrder(merchantId, total);
        return "redirect:"+pageRedirectionData;
    }

    @RequestMapping("/getOrder")
    @ResponseBody
    public AlipayTradeQueryResponse getOrder(String outTradeNo){
        AlipayTradeQueryResponse res = alipayService.getOrder(outTradeNo);
        return res;
    }


    @RequestMapping("/callBack")
    @ResponseBody
    public void callBack(@RequestBody CallBackVO vo){
            alipayService.callBack(vo.getOutTradeNo());
    }

    @RequestMapping("/callBack2")
    @ResponseBody
    public void callBack2(String outTradeNo){
        alipayService.callBack2(outTradeNo);
    }

    @RequestMapping("/test")
    @ResponseBody
    public void test(){
        // 发送消息
        orderFeignClient.saveOrder(new ArrayList<>());
    }
//    @RequestMapping("/test1")
//    public void test1(){
//        HashMap<String, String> map = new HashMap<>();
//        map.put("value1","1");
//        map.put("value2","2");
//        redisTemplate.opsForHash().putAll("test",map);
//    }
//
//    @RequestMapping("/test2")
//    public void test2(){
//        redisTemplate.opsForHash().put("test","value1","2");
//    }

}
