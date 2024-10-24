package com.cykj.mqConsumer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Component;

@Component
public class AlipaySendCallBack implements SendCallback {
    @Override
    public void onSuccess(SendResult sendResult) {
        System.out.println("发送成功");
    }

    @Override
    public void onException(Throwable throwable) {
        System.out.println("发送异常");
        throwable.printStackTrace();
    }
}
