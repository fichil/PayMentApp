package com.cykj.mqConsumer;

import com.cykj.config.WebSocketConfig;
import com.cykj.controller.WeChatPayController;
import com.cykj.service.WeChatPayService;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PrivateKey;
import org.apache.http.client.methods.HttpGet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@RocketMQMessageListener(topic = "createOrder",consumerGroup = "${rocketmq.consumer.pay-group}")
@Component
public class OrderConsumer implements RocketMQListener<String> {
    @Autowired
    private  WebSocketConfig.CustomWebSocketHandler customWebSocketHandler;

    @Override
    public void onMessage(String sessionId) {
        System.out.println("消费了");
        WebSocketSession session = customWebSocketHandler.getSessionsMap().get(sessionId);
        try {
            System.out.println(customWebSocketHandler.getSessionsMap());
            session.sendMessage(new TextMessage("ok"));
            customWebSocketHandler.getSessionsMap().remove(sessionId);
            System.out.println(customWebSocketHandler.getSessionsMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
