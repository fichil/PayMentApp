//package com.cykj;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.aliyun.documentautoml20221229.Client;
//import com.aliyun.teaopenapi.models.Config;
//import com.cykj.utils.AliCloud;
//import com.cykj.utils.TimeUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
//import com.wechat.pay.contrib.apache.httpclient.auth.*;
//import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
//import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
//
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.StandardCharsets;
//import java.security.PrivateKey;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import java.util.*;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.UUID;
//
//import static org.springframework.core.NestedExceptionUtils.buildMessage;
//import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
//import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
//
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.junit.Test;
//import org.junit.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//
//@SpringBootTest
//public class MyTest {
////    //    mchid商户号
////    //
////    private CloseableHttpClient httpClient;
////    private CertificatesManager certificatesManager;
////    // private Verifier verifier;
////
////    private AutoUpdateCertificatesVerifier verifier;
////    String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
////            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC9flEvcFvmimai\n" +
////            "oCUA54iR+DhHaiqovOFuAX9gTP6MDYqyog3RBfrcZF4IKpCKWTLj4ZH1lM/ZceJM\n" +
////            "uAx7VrSrJD1HuDC/zj4NgVmhU5fKi25BBrtRMcTJEz0+7RA/XxpRXiKfT6WqTfvv\n" +
////            "uMk0hEorFk/olcg6K6gC7uO34o537qZBEG4dfP6EXZyPC336c3090Guz9r/5+k2+\n" +
////            "nxi+7r4T1kdALAEp2ncMuLrL0pJwOX8YwSsHWxNQFHzwT3pNFbsm7ZIHeSrHzG/N\n" +
////            "eh+OOCdMU2KAPWWkl0z47P4v9z2n1faiujAvnf7Dh4r/B9AjZyHxRwozgmr6HzaU\n" +
////            "/MA+yc6zAgMBAAECggEBAI7Qkf1v8EN7kckFa34YAk0JR9lDAa201S3kgQyiWsJR\n" +
////            "PnRNQJ6FpEDQgDIlalRqz7kB+9Cb/F7EvyrVF8spWPcXU/wmrM8ScKAbGeNFrkQf\n" +
////            "+9OUcD7MhKSLFIDFiJZKZfpnOS7ah2AocDWHDlW/UmrQ4R7JLGPzHShAxW2EmZ12\n" +
////            "+uVhBXPi0YRr1q2ISXfmJABQ+FNGc+AG0L+3HJfzzDeeeiXA/VR4C5cqRfr/OrpN\n" +
////            "+1TE20+9GJ5wj3jOPiwPUxOZ1t/kxRGlrPdaaklIngKdv5k/uH/Cu9b6JGfP4YJu\n" +
////            "SF3pifHJEBA69zVfedvoXHRlGxxyevTI1/CQf1Y9whkCgYEA3HZhAcaLdVbvoBqZ\n" +
////            "uec5HPAg6EhZp78/eGVLyK6F4Jd6XG+5kxNIQX8Tr0s9D+1A+PEVgewaqCA9FLg/\n" +
////            "WUeZGc8JdPqVT2O4S+1r64UrIGEVbNWBBLBDaXC9A02NbFhcW0ciF25loy27v5FO\n" +
////            "qOGxZFWt5UYH8CExHcSgrpPIkncCgYEA3An4GVc/dX8lmyy5K8d5+0PmgDX7YFo6\n" +
////            "YDj4HMtYXffdTLk+CmcV+r3aDMaf+xGZ4XAXLLaY4lWSeYcuiDjgy28oWsJ0bfjd\n" +
////            "uLKMJm0zck15pSEp97vClR7+tY2ABQN+lrUryOZbEY0AIScBAhaE/lAJ6AvTmR1A\n" +
////            "mvMigLlJ2KUCgYBQsNdSogvbF8W+8N2BKHtD9zPkMM730cz2UQUK363KhUbY3F+s\n" +
////            "0CrrJgrbZwtjb/AO7ZZ0pikfjiUtFz7Ae68yvg91DF5n31TOHYkZe/VL9Zy05/Q/\n" +
////            "9g/GYOgddF4l4LV1BzUYJGT48707IYEL2QdndOCgASDYqB1lCtnQ6/BGgwKBgAEf\n" +
////            "E+KdLTeeFET8L11nwNlk+THCFKvjCdUftTQAXUztTHVtpEFNUTFb8laKVJpk9pHN\n" +
////            "jnCt5Y2RvhNcewI6t/qRcHU19H3y/L5KOqifedmlR29uAda/YDh+VfH67KIZ6HVA\n" +
////            "Kx70/ZXTUCLbHggysS7SnZpPJR7spT2k65IYX6X5AoGARlK5fWpgW5W2D9iwX5Xr\n" +
////            "F0d9i/X/GH7Jl46IfnF2AfQq+FG/yaBqQSX1SxD0bZ9mtzX1DvIEVJaEhLrInDpy\n" +
////            "C2wF9h4CTUM5IywvFwg0oAVExS8rfaC+1ITykUqp0kwST/b7feoUO56cHH6oQYBP\n" +
////            "V505tAN1y46r6LbIGvsPrHo=\n" +
////            "-----END PRIVATE KEY-----";//私钥
////    String merchantId = "1611845480";//商户号
////    String merchantSerialNumber = "6C73136A86F7E686E6526C0E698014C6245D01D0";//商户序列化证书
////    String apiV3Key = "e10adc3949ba59abbe56e057f20f883e";
////
////
////
////    @Autowired
////    RedisTemplate<String,Object> redisTemplate;
////
////    //    @Before
//////    public void setup() throws Exception {
//////        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKey);
//////        // 获取证书管理器实例
//////        certificatesManager = CertificatesManager.getInstance();
//////        // 向证书管理器增加需要自动更新平台证书的商户信息
//////        certificatesManager.putMerchant(merchantId, new WechatPay2Credentials(merchantId,
//////                        new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
//////                apiV3Key.getBytes(StandardCharsets.UTF_8));
//////        // 从证书管理器中获取verifier
//////        verifier = certificatesManager.getVerifier(merchantId);
//////        httpClient = WechatPayHttpClientBuilder.create()
//////                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
//////                .withValidator(new WechatPay2Validator(certificatesManager.getVerifier(merchantId)))
//////                .build();
//////    }
////@Before
////public void setup() throws Exception{
////    // 加载商户私钥（privateKey：私钥字符串）
////    PrivateKey merchantPrivateKey = PemUtil
////            .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
////
////    //使用自动更新的签名验证器，不需要传入证书
////    verifier = new AutoUpdateCertificatesVerifier(
////            new WechatPay2Credentials(merchantId, new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
////            apiV3Key.getBytes(StandardCharsets.UTF_8));
////
////    httpClient = WechatPayHttpClientBuilder.create()
////            .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
////            .withValidator(new WechatPay2Validator(verifier))
////            .build();
////
////}
////    @Test
////    public void createOrder() throws Exception {
////        // 加载商户私钥（privateKey：私钥字符串）
////        PrivateKey merchantPrivateKey = PemUtil
////                .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
////        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
////        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
////                new WechatPay2Credentials("1611845480", new PrivateKeySigner("6C73136A86F7E686E6526C0E698014C6245D01D0", merchantPrivateKey)),
////                "e10adc3949ba59abbe56e057f20f883e".getBytes("utf-8"));
//////        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
//////                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
//////                .withValidator(response -> true) // NOTE: 设置一个空的应答签名验证器，**不要**用在业务请求
//////                .build();
////
////        // 初始化httpClient
////        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
////                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
////                .withValidator(new WechatPay2Validator(verifier)).build();
////        //下单的请求接口
////        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
////        httpPost.addHeader("Accept", "application/json");
////        httpPost.addHeader("Content-type", "application/json; charset=utf-8");// 空格可能有问题
////        System.out.println("11111111111111111111");
////        ByteArrayOutputStream bos = new ByteArrayOutputStream();
////        ObjectMapper objectMapper = new ObjectMapper();
////        ObjectNode rootNode = objectMapper.createObjectNode();
////        rootNode.put("mchid", "1611845480")
////                .put("appid", "wx96600d2c9189b7bb")
////                .put("description", "Image形象店-深圳腾大-QQ公仔9")//商品描述
////                .put("notify_url", "http://ccj.nat300.top/wxPay/wx-oauth-code-return")//回调接口
////                .put("out_trade_no", System.currentTimeMillis() + "2");//自己平台唯一的订单号
////        rootNode.putObject("amount")
////                .put("total", 1);//支付总金额，已分为单位，传的是json对象
////        rootNode.putObject("payer")
////                .put("openid", "oVoUS6RDdtAr65-COpiN3SC34RpY");//支付总金额，已分为单位，传的是json对象
////
////        try {
////            objectMapper.writeValue(bos, rootNode);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        try {
////            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
////        CloseableHttpResponse response = null;
////        try {
////            response = httpClient.execute(httpPost);
////        } catch (IOException e) {
////            e.printStackTrace();
////
////        }
////        String bodyAsString = null;
////        try {
////            bodyAsString = EntityUtils.toString(response.getEntity());
////        } catch (IOException e) {
////            e.printStackTrace();
////
////        }
////        System.out.println(bodyAsString);
////    }
////    @Test
////    public void test() {
////        String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/a2frka7nqzreas1znayrtkcvmu29sndd"  + "?mchid=" + merchantId;
////        CloseableHttpClient httpClient = null;
////        HttpGet httpGet = null;
////        try {
////            URIBuilder uriBuilder = new URIBuilder(url);
////            httpGet = new HttpGet(uriBuilder.build());
////            httpGet.addHeader("Accept","application/json");
////            // 加载商户私钥（privateKey：私钥字符串）
////            PrivateKey merchantPrivateKey = PemUtil
////                    .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
////            // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
////            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
////                    new WechatPay2Credentials("1611845480", new PrivateKeySigner("6C73136A86F7E686E6526C0E698014C6245D01D0", merchantPrivateKey)),
////                    "e10adc3949ba59abbe56e057f20f883e".getBytes("utf-8"));
////            // 初始化httpClient
////            httpClient = WechatPayHttpClientBuilder.create()
////                    .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
////                    .withValidator(new WechatPay2Validator(verifier)).build();
//////
////            CloseableHttpResponse response = httpClient.execute(httpGet);
////            String bodyAsString = EntityUtils.toString(response.getEntity());
////            System.out.println(bodyAsString);
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }finally {
////            try {
////                httpClient.close();
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
////        }
////    }
////
////    @Test
////    public void test1(){
////        HashMap<String, String> map = new HashMap<>();
////        map.put("value1","1");
////        map.put("value2","2");
////        redisTemplate.opsForHash().putAll("test",map);
////    }
//
//    @Test
//    public void test2() throws Exception {
//        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
//        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
//        Config config = new Config()
//                .setType("access_key")
//                .setAccessKeyId(AliCloud.ak)
//                .setAccessKeySecret(AliCloud.sk);
//        // Endpoint 请参考 https://api.aliyun.com/product/documentAutoml
//        config.endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";
//        Client client = new Client(config);
//        System.out.println(client);
//    }
//
//}
