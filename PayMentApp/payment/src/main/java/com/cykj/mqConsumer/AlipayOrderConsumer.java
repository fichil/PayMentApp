package com.cykj.mqConsumer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.cykj.service.AlipayService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RocketMQMessageListener(topic = "jx2403",consumerGroup = "${rocketmq.consumer.order-group}")// order-consumer
@Component
public class AlipayOrderConsumer implements RocketMQListener<String> {
    @Autowired
    AlipayService alipayService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public void onMessage(String s) {
        AlipayTradeQueryResponse response = alipayService.getOrder(s);
        if (response.isSuccess()) {
            if (response.getTradeStatus().equals("TRADE_SUCCESS")){
                System.out.println("延时消息送达.");
                Map<Object, Object> order = redisTemplate.opsForHash().entries(s);
                if (order != null){ // 可能是一个无用的判断
                    if (order.size() > 3){ // redisTemplate.opsForHash().entries(s)不会返回null值所以判断map大小
                        redisTemplate.opsForHash().put(s, "transactionId", response.getTradeNo());
                        redisTemplate.opsForHash().put(s, "state", 1);
                    }
                }
            }else {
                System.out.println("其他的订单状态");
            }
        } else {
            System.out.println("调用失败");
        }
    }

}
