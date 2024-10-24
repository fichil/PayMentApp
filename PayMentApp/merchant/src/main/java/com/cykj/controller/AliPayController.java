package com.cykj.controller;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.cykj.utils.AliPay;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AliPayController {
//    @RequestMapping("/createOrder")
    public String createOrder(HttpServletResponse res){
        DefaultAlipayClient alipayClient = AliPay.getAlipayClient();
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
//异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl("http://hhxzty.natappfree.cc/imgs/404.html");
//        request.setNotifyUrl("");
//同步跳转地址，仅支持http/https
        request.setReturnUrl("http://hhxzty.natappfree.cc/imgs/success.html");// 支付成功点击完成的跳转

//        request.setReturnUrl("");
/******必传参数******/
        JSONObject bizContent = new JSONObject();
        Snowflake snowflake = new Snowflake(0,1);
//商户订单号，商家自定义，保持唯一性
        System.out.println("-------------------------------------------");
        long l = snowflake.nextId();
        System.out.println("-------------------------------------------");
        System.out.println(l);
//        bizContent.put("out_trade_no", snowflake.nextId());
        bizContent.put("out_trade_no", l);
//支付金额，最小值0.01元
        bizContent.put("total_amount", 0.01);
//订单标题，不可使用特殊符号
        bizContent.put("subject", "测试商品");

/******可选参数******/
        //手机网站支付默认传值QUICK_WAP_WAY
        bizContent.put("product_code", "QUICK_WAP_WAY");
        bizContent.put("app_id", AliPay.getAPPID()); // 应用id决定哪个商户收到钱
        bizContent.put("time_expire", "2024-10-10 22:00:00");

        bizContent.put("timeout_express","5m");
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
        } else {
            System.out.println("调用失败");
        }

        return "redirect:"+pageRedirectionData;
    }

//    @RequestMapping("/getOrder")
//    @ResponseBody
    public String getOrder(String outTradeNo){
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

        if (response.isSuccess()) {
            System.out.println("调用成功");
            return response.getBody();
        } else {
            System.out.println("调用失败");
            return null;
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            // System.out.println(diagnosisUrl);
        }
    }

//    @RequestMapping("/closeOrder")
//    @ResponseBody
//    public void closeOrder(String outTradeNo){
//        DefaultAlipayClient alipayClient = AliPay.getAlipayClient();
//        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
//        request.setNotifyUrl("http://hhxzty.natappfree.cc/imgs/404.html");
//        request.setReturnUrl("http://hhxzty.natappfree.cc/imgs/404.html");
//        request.setApiVersion("1.0");
//        JSONObject bizContent = new JSONObject();
//        bizContent.put("app_id", AliPay.getAPPID());
//        bizContent.put("out_trade_no", outTradeNo);
//        request.setBizContent(bizContent.toString());
////        AlipayTradeCloseModel alipayTradeCloseModel = new AlipayTradeCloseModel();
////        alipayTradeCloseModel.setOutTradeNo("14648125413123123444123");
////        request.setBizModel(alipayTradeCloseModel);
//        AlipayTradeCloseResponse response = null;
//        try {
////            response = alipayClient.execute(request); // 公钥模式加签调用
//            response = alipayClient.certificateExecute(request); // 公钥证书模式加签调用
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        System.out.println(response.getBody());
//        if(response.isSuccess()){
//            System.out.println("调用成功");
//        } else {
//            System.out.println("--------------调用失败");
//             String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
//             System.out.println(diagnosisUrl);
//            System.out.println("-----------------");
//        }
//    }
}
