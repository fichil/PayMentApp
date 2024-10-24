package com.cykj.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.feign.OrderFeignClient;
import com.cykj.pojo.MainDbOrder;
import com.cykj.utils.ResponseDTO;
import com.cykj.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class InsertOrderService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private WeChatPayService weChatPayService;
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private OrderFeignClient orderFeign;


    // 定时任务，每隔30分钟执行一次
    @Scheduled(fixedRate = 300000)  // 3分钟
    public void storeOrdersFromRedisToDb() {
//        // 获取所有订单的 Redis key，使用通配符查找所有订单
        Set<String> keys = redisTemplate.keys("*");
        List<MainDbOrder> orders = new ArrayList<>();
        if (keys != null) {
            for (String orderId : keys) {
                // 获取每个订单的哈希数据
                Map<Object, Object> orderData = redisTemplate.opsForHash().entries(orderId);

                if (!orderData.isEmpty()) {
                    String state = (String) orderData.get("state");
                    if (state != null) {
                        if (state.equals("1")) {//不为1就是mq还没操作过
                            System.out.println(state);
                            //将数据存储进去
                            String outTradeNo = (String) orderData.get("outTradeNo");
                            String transactionId = (String) orderData.get("transactionId");
                            String total = (String) orderData.get("total");
                            String merchantId = (String) orderData.get("Mid");
                            String channel = (String) orderData.get("payment");
                            int i = Integer.parseInt(channel);
                            BigDecimal bigDecimal = new BigDecimal(total);
                            System.out.println("存入订单号" + outTradeNo);
                            System.out.println("订单id" + transactionId);
                            System.out.println("total" + total);
                            System.out.println("商家" + merchantId);
                            MainDbOrder mainDbOrder = new MainDbOrder();
                            mainDbOrder.setMerchantNumber(merchantId);//商家id
                            mainDbOrder.setOrderNumber(outTradeNo);
                            mainDbOrder.setCustomerAccount(transactionId);
                            mainDbOrder.setChannel(i);
                            mainDbOrder.setPrice(bigDecimal);
                            orders.add(mainDbOrder);


                            System.out.println("插入数据库了");
                            // 从 Redis 中删除该订单数据
                            redisTemplate.delete(orderId);
                        } else {
                            //判断有没有大于15分钟
                            String value = (String) redisTemplate.opsForHash().get(orderId, "time");
                            long number = Long.parseLong(value);//当时记录的时间
                            System.out.println("时间" + number);
                            long currentTime = TimeUtil.getCurrentTime();//当前时间
                            System.out.println("当前时间" + currentTime);
                            long intervalTime = currentTime - number;
                            if (intervalTime > 900000) {//大于15分钟了,有异常
                                String payment = (String) redisTemplate.opsForHash().get(orderId, "payment");
                                if (payment.equals("1")) {//调用微信查询接口
                                    String s = weChatPayService.queryOrder(orderId);
                                    wxPay(orderId, s, orderData);
                                } else if (payment.equals("0")) {//调用支付宝查询接口
                                    if (alipayService.callBack(orderId)) {
                                        redisTemplate.opsForHash().put(orderId, "state", 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (orders.size() > 0) {
            ResponseDTO dto = orderFeign.saveOrder(orders);
            orderFeign.addOrder(orders);
            if (dto == null) {
                orderFeign.saveOrder(orders);
            }
        }

        // 日志信息
        System.out.println("Successfully stored Redis orders to DB and cleared Redis data.");

    }

    public void wxPay(String orderId, String s, Map<Object, Object> orderData) {
        JSONObject jsonObject = JSON.parseObject(s);
        String tradeState = (String) jsonObject.get("trade_state");
        if (tradeState.equals("SUCCESS")) {
            String transactionId = (String) jsonObject.get("transaction_id");
            orderData.put("transactionId", transactionId);//系统返回,
            orderData.put("state", "1");//1表示队列检查过了
            redisTemplate.opsForHash().putAll(orderId, orderData);
        } else if (tradeState.equals("NOTPAY")) {
            System.out.println("关单");
            weChatPayService.closeOrder(orderId);//未支付，关闭此笔交易
            //从redis里面删掉
            redisTemplate.delete(orderId);
        }
    }


}
