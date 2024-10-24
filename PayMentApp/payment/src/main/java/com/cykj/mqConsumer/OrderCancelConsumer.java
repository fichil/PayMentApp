package com.cykj.mqConsumer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.service.WeChatPayService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RocketMQMessageListener(topic = "cancelOrder",consumerGroup = "${rocketmq.consumer.order-cancel-group}")
@Component
public class OrderCancelConsumer implements RocketMQListener<String> {
    @Autowired
    private WeChatPayService weChatPayService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;//对redis进行操作

    @Override
    public void onMessage(String tradeNo) {

        System.out.println("延时队列收到的订单号" + tradeNo);
        /*
        调用查询接口查询此笔订单状态，支付了就在查下数据库？redis有没有存，避免回调没有执行。未支付就取消此笔订单
         */
        String s = weChatPayService.queryOrder(tradeNo);
        JSONObject jsonObject = JSON.parseObject(s);
        String tradeState = (String)jsonObject.get("trade_state");
        String transactionId = (String)jsonObject.get("transaction_id");
        System.out.println("延时队列查询的返回订单状态" + tradeState);
        if(tradeState != null) {
            if(tradeState.equals("SUCCESS")) {
                System.out.println("支付成功,到延时队列检查了");
                String state = (String)redisTemplate.opsForHash().get(tradeNo, "state");
                System.out.println("状态为" + state);
                //直接那这个查他的状态是不是0,是2就是没执行回调，那就改成1，将订单号加进去，那如果状态是0就改为1
              if(state.equals("2")) {//2是没执行到回调
                  Map<Object, Object> orderMap = redisTemplate.opsForHash().entries(tradeNo);
                  orderMap.put("transactionId",transactionId);//系统返回,
                  orderMap.put("state","1");//1表示队列检查过了
                  redisTemplate.opsForHash().putAll(tradeNo,orderMap);
              } else if (state.equals("0")) {//0执行回调了
                  Map<Object, Object> orderMap = redisTemplate.opsForHash().entries(tradeNo);
                  orderMap.put("state","1");//1表示队列检查过了
                  redisTemplate.opsForHash().putAll(tradeNo,orderMap);
              }


            } else if (tradeState.equals("NOTPAY")){
                System.out.println("订单未支付");
               // weChatPayService.closeOrder(tradeNo);//未支付，关闭此笔交易
                //从redis里面删掉
            }
        }

    }



}
