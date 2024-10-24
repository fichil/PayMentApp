package com.cykj.service.Impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.cykj.feign.OrderFeignClient;
import com.cykj.mqConsumer.AlipayOrderConsumer;
import com.cykj.mqConsumer.AlipaySendCallBack;
import com.cykj.service.AlipayService;
import com.cykj.utils.AliPay;
import com.cykj.utils.ResponseDTO;
import com.cykj.utils.TimeUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    OrderFeignClient orderFeignClient;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Autowired
    AlipaySendCallBack alipaySendCallBack;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public String createOrder(String merchantNumber, String price) {
        DefaultAlipayClient alipayClient = AliPay.getAlipayClient();
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
//异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl("http://hhxzty.natappfree.cc/imgs/404.html");

/******必传参数******/
        JSONObject bizContent = new JSONObject();
//        在多实例部署的情况下，要保证workerId与dataCenterId的组合是不一致的
        Snowflake snowflake = new Snowflake(0,1);
//      商户订单号，商家自定义，保持唯一性
        long l = snowflake.nextId();
        System.out.println("out_trade_no="+l);

//        同步跳转地址，仅支持http/https，在这里选择触发一个接口看看能不能接收到回调参数记录到数据库
//        request.setReturnUrl("http://ccj.nat300.top/aliPay/callBack2?outTradeNo="+String.valueOf(l));// 支付成功点击完成的跳转
        request.setReturnUrl("http://hutu.natapp1.cc/#/SuccessPage?outTradeNo="+String.valueOf(l));// 支付成功点击完成的跳转
        bizContent.put("out_trade_no", l);
//      支付金额，最小值0.01元
        bizContent.put("total_amount", price);
//      订单标题，不可使用特殊符号
        bizContent.put("subject", "测试商品");

/******可选参数******/
        //  手机网站支付默认传值QUICK_WAP_WAY
        bizContent.put("product_code", "QUICK_WAP_WAY");
        bizContent.put("app_id", AliPay.getAPPID()); // 应用id决定哪个商户收到钱
        bizContent.put("time_expire", TimeUtil.getExpireTime());

//        bizContent.put("timeout_express","5m");
        bizContent.put("quit_url","http://baidu.com");
        bizContent.put("return_url","http://baidu.com");
        bizContent.put("notify_url","http://hhxzty.natappfree.cc/imgs/404.html");

        //// 商品明细信息，按需传入
        JSONArray goodsDetail = new JSONArray();
        JSONObject goods1 = new JSONObject();
        goods1.put("goods_id", "goodsNo1");
        goods1.put("goods_name", "子商品1");
        goods1.put("quantity", 1);
        goods1.put("price", 0.01);
        goodsDetail.add(goods1);
        bizContent.put("goods_detail", goodsDetail);

        request.setBizContent(bizContent.toString());
        AlipayTradeWapPayResponse response = null;
        String pageRedirectionData = null;
        try {
            // POST请求返回html表单
            //            response = alipayClient.pageExecute(request,"POST");
            // GET请求返回支付宝URL
            // AlipayTradeWapPayResponse response = alipayClient.pageExecute(request,"GET");
            response = alipayClient.pageExecute(request,"GET");
            pageRedirectionData = response.getBody();
            System.out.println(pageRedirectionData);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        if(response.isSuccess()){
            System.out.println("调用成功");
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("outTradeNo",l);
            orderMap.put("total",price);
            orderMap.put("Mid",merchantNumber);
            orderMap.put("state",2);//就是队列还没检查过，检查过就会变为1
            orderMap.put("payment",0);
            orderMap.put("time",TimeUtil.getCurrentTime());
            redisTemplate.opsForHash().putAll(String.valueOf(l),orderMap);
            rocketMQTemplate.asyncSend("jx2403", MessageBuilder.withPayload(l).build(),alipaySendCallBack,1000,6);
            return  pageRedirectionData;
        } else {
            System.out.println("调用失败");
            return "error";
        }
    }

    @Override
    public AlipayTradeQueryResponse getOrder(String outTradeNo) {
        DefaultAlipayClient alipayClient = AliPay.getAlipayClient();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        request.setReturnUrl("http://hhxzty.natappfree.cc");
        request.setNotifyUrl("http://hhxzty.natappfree.cc");
        // 设置订单支付时传入的商户订单号
        model.setOutTradeNo(outTradeNo);
        // 设置查询选项
        List<String> queryOptions = new ArrayList<String>();
        queryOptions.add("trade_settle_info");
        model.setQueryOptions(queryOptions);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(response.getBody());

        return response;
    }

    /**
     * @description: 支付宝回调函数
     * @param:
     * @return:
     */
    @Override
    public boolean callBack(String outTradeNo) {
        AlipayTradeQueryResponse response = getOrder(outTradeNo);
        if (response.isSuccess()) {
            if (response.getTradeStatus().equals("TRADE_SUCCESS")){
                Map<Object, Object> order = redisTemplate.opsForHash().entries(outTradeNo);
                if (order != null){
                    redisTemplate.opsForHash().put(outTradeNo, "transactionId", response.getTradeNo());
                    redisTemplate.opsForHash().put(outTradeNo, "state", 0);// 回调改动状态码0
                    return true;
                }
            }else {
                    System.out.println("其他的订单状态");
                    return false;
            }
        } else {
            System.out.println("调用失败");
            redisTemplate.delete(outTradeNo);
            return false;
        }
        return false;
    }

    /**
     * @description: 支付宝回调函数
     * @param:
     * @return:
     */
    @Override
    public String callBack2(String outTradeNo) {
        AlipayTradeQueryResponse response = getOrder(outTradeNo);
        if (response.isSuccess()) {
            if (response.getTradeStatus().equals("TRADE_SUCCESS")){
                Map<Object, Object> order = redisTemplate.opsForHash().entries(outTradeNo);
                if (order != null){
                    redisTemplate.opsForHash().put(outTradeNo, "transactionId", response.getTradeNo());
                    redisTemplate.opsForHash().put(outTradeNo, "state", 0);// 回调改动状态码0
//                    return "redirect:http://hutu.natapp1.cc/#/success";
                    return "true";
                }
            }else {
                System.out.println("其他的订单状态");
                return "false";
            }
        } else {
            System.out.println("调用失败");
            redisTemplate.delete(outTradeNo);
            return "false";
        }
        return "false";
    }
}
