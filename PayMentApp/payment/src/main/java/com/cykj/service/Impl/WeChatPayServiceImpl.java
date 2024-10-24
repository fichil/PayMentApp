package com.cykj.service.Impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.service.WeChatPayService;
import com.cykj.utils.TimeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
@Service
public class WeChatPayServiceImpl implements WeChatPayService {
    @Value("${apiV3Key}")
    String apiV3Key;//apiv3秘钥
    @Value("${privateKey}")
    private String privateKey;
    @Value("${appId}")
    String appId;//appId
    @Value("${mchId}")
    String mchId;//商户号
    @Value("${mchSerialNo}")
    String merchantSerialNumber;//商户序列化证书
    @Autowired
    private RocketMQTemplate rocketMQTemplate;//对任务队列进行操作

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;//对redis进行操作

    /**
     * 调起支付
     * @param state
     * @param openId
     * @return
     */
    @Override
    public ModelAndView createOrder(String state,String openId) {
        String[] split = state.split("=");
        String notifyUrl = "http://ccj.nat300.top/wxPay/vxCallBack";//支付成功回调地址
        System.out.println("回调地址" + notifyUrl);
        double number = Double.parseDouble(split[0]) * 100;
        Integer total = (int) number;
        CloseableHttpClient httpClient = null;
        AutoUpdateCertificatesVerifier verifier = null;
        CloseableHttpResponse response = null;
        ByteArrayOutputStream bos = null;
        try {
            // 加载商户私钥（privateKey：私钥字符串）
            PrivateKey merchantPrivateKey = PemUtil
                    .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
            // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
            verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(mchId, new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
                    apiV3Key.getBytes("utf-8"));
            // 初始化httpClient，会自己帮你生成签名并加到请求中
            httpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant(mchId, merchantSerialNumber, merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(verifier)).build();
            //下单的请求接口，获取prepay_id
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
            httpPost.addHeader("Accept", "application/json");//http头参数必填项
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");//http头参数必填项
            bos = new ByteArrayOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();//使用的是jackson
            ObjectNode rootNode = objectMapper.createObjectNode();//JSON 对象的类
            //JSONObject jsonObject = new JSONObject();  //使用的fastjson


            String order = RandomUtil.randomString(32) + "";//订单号，使用hutool工具包生成一个32位随机数
            System.out.println();
            rootNode.put("mchid", mchId)
                    .put("appid", appId)
                    .put("description", "测试")//商品描述
                    .put("notify_url", notifyUrl)//回调接口
                    .put("out_trade_no", order)
                    .put("attach",split[1]);//自己平台唯一的订单号
            rootNode.putObject("amount")
                    .put("total", total);//支付总金额，已分为单位，传的是json对象
            rootNode.putObject("payer")
                    .put("openid", openId);//需要自己的openid
            //将rootNode转换为 JSON 字符串writeValue 方法会处理序列化过程并将结果写入到 bos。最终，bos 将包含以 JSON 格式表示的 rootNode 数据。
            objectMapper.writeValue(bos, rootNode);
            //创建一个新的 StringEntity 对象，使用 bos 中的内容作为请求体。bos.toString("UTF-8") 将字节数组转换为字符串,设置 HTTP POST 请求的实体（即请求体）为刚刚创建的 StringEntity
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            //发送 HTTP POST 请求并获取响应。httpClient 是之前构建的 HTTP 客户端，execute 方法会执行请求并返回响应对象
            response = httpClient.execute(httpPost);
            //从响应中提取实体部分并将其转换为字符串。EntityUtils 是一个工具类，提供了将响应实体转换为字符串的方法
            String bodyAsString = EntityUtils.toString(response.getEntity());
            //将响应的 JSON 字符串解析为JsonNode对象
            JsonNode node = objectMapper.readTree(bodyAsString);

            //得到prepay_id,调起支付接口
            String timeStamp = (System.currentTimeMillis() / 1000) + "";//时间戳
            String nonce = RandomUtil.randomString(20);//随机字符串，长度不大于32位
            StringBuffer buffer = new StringBuffer();//构建签名
            buffer.append(appId).append("\n");//应用id
            buffer.append(timeStamp).append("\n");//时间戳
            buffer.append(nonce).append("\n");//随机字符串
            String prepayId = "prepay_id=" + node.get("prepay_id").asText();//获得prepay_id
            buffer.append(prepayId).append("\n");//prepay_id
            //对签名进行加密
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(merchantPrivateKey);
            sign.update(buffer.toString().getBytes());
            String paySign = Base64.getEncoder().encodeToString(sign.sign());
            System.out.println("prepayId" + prepayId);
            ModelAndView mv = new ModelAndView("wxpay");
            mv.addObject("appId", appId);//appid
            mv.addObject("timeStamp", timeStamp);//时间戳
            mv.addObject("nonceStr", nonce);//随机字符串
            mv.addObject("packageStr", prepayId);  // 不能直接使用 'package'
            //mv.addObject("signType", jsapiPayParam.get("signType"));
            mv.addObject("paySign", paySign);//签名

            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("outTradeNo",order);//预生产订单号
            orderMap.put("total",split[0]);
            orderMap.put("Mid",split[1]);//商户号
            orderMap.put("state","2");//就是队列还没检查过，检查过就会变为1
            orderMap.put("payment","1");
            orderMap.put("time", TimeUtil.getCurrentTime());
            redisTemplate.opsForHash().putAll(order,orderMap);
            //生成订单号后将订单号延时发到队列，检查数据库是否有正确记录，防止回调函数没执行到
            //创建一个消息对象
            GenericMessage<String> message = new GenericMessage<String>(order);
            //发送延时消息
            rocketMQTemplate.asyncSend("cancelOrder",message,null,2000,5);//延时1分钟发送
            System.out.println("商户订单号" + order);


            return mv;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int closeOrder(String orderNumber) {
        CloseableHttpClient httpClient = null;
        AutoUpdateCertificatesVerifier verifier = null;
        try {
            // 加载商户私钥（privateKey：私钥字符串）
            PrivateKey merchantPrivateKey = PemUtil
                    .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
            // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
            verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(mchId, new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
                    apiV3Key.getBytes("utf-8"));
            // 初始化httpClient，会自己帮你生成签名并加到请求中
            httpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant(mchId, merchantSerialNumber, merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(verifier)).build();

            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/"+ orderNumber + "/close");
            httpPost.addHeader("Accept","application/json");
            httpPost.addHeader("Content-type","application/json");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("mchid",mchId);
            objectMapper.writeValue(bos,rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"),"UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println(response.getStatusLine().getStatusCode());
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    @Override
    public ResponseEntity<Map> vxCallBack(HttpServletRequest request) {
        System.out.println("回调了o");
        Map result = new HashMap();
        String timeStamp = request.getHeader("Wechatpay-Timestamp");//时间戳
        String nonce = request.getHeader("Wechatpay-Nonce");//随机字符串
        String serial = request.getHeader("Wechatpay-Serial");//商户证书序列号
        String signature = request.getHeader("Wechatpay-Signature");//签名串
        try {
            // 加载商户私钥（privateKey：私钥字符串）
            PrivateKey merchantPrivateKey = PemUtil
                    .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
            // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials("1611845480", new PrivateKeySigner("6C73136A86F7E686E6526C0E698014C6245D01D0", merchantPrivateKey)),
                    "e10adc3949ba59abbe56e057f20f883e".getBytes("utf-8"));
            StringBuilder signStr = new StringBuilder();
            signStr.append(timeStamp).append("\n");
            signStr.append(nonce).append("\n");
            //获取请求报文
            BufferedReader br = null;
            br = request.getReader();
            String str = null;
            StringBuilder builder = new StringBuilder();
            while ((str = br.readLine()) != null) {
                builder.append(str);
            }
            signStr.append(builder.toString()).append("\n");
            //verify方法验证签名，以确保接收到的数据来自合法的微信支付平台，并且在传输过程中没有被篡改，返回一个布尔值
            boolean verify = verifier.verify(serial, signStr.toString().getBytes(), signature);

            //判断验证结果，对则解析密文做相应操作
            if (verify) {
                //解密密文
                JSONObject jsonObject = JSON.parseObject(builder.toString());
                System.out.println("回调参数" + jsonObject);
                String resource = jsonObject.get("resource").toString();//获取resource的值，是json格式的字符串
                JSONObject jsonObject1 = JSON.parseObject(resource);//转为json对象
                String ciphertext= jsonObject1.get("ciphertext").toString();
                String associatedData = jsonObject1.get("associated_data").toString();
                String resourceNonce = jsonObject1.get("nonce").toString();

                //AesUtil用于执行 AES 加密和解密操作，具体是使用微信支付的 API V3 密钥（apiV3Key）进行数据加密和解密
                AesUtil util = new AesUtil(apiV3Key.getBytes("utf-8"));
                String orderInformation = util.decryptToString(associatedData.getBytes(), resourceNonce.getBytes(), ciphertext);//对密文进行解密
                System.out.println("解密后" + orderInformation);//解密后的数据，里面有订单号等数据
                JSONObject jsonObject2 = JSON.parseObject(orderInformation);
                System.out.println(jsonObject2);
                String outTradeNo = (String)jsonObject2.get("out_trade_no");
//              System.out.println("自定义订单号" + outTradeNo);

                String transactionId = (String)jsonObject2.get("transaction_id");
                System.out.println("订单号" + transactionId);

                Map<Object, Object> orderMap = redisTemplate.opsForHash().entries(outTradeNo);
                String state = (String)orderMap.get("state");
                if (state != null) {
                    if(state.equals("1")) {
                        return ResponseEntity.ok().build(); // 返回 200 OK，这里需要返回一个200或者204
                    } else {
                        orderMap.put("transactionId",transactionId);//系统返回,
                        orderMap.put("state","0");//回调里面改为0
                        redisTemplate.opsForHash().putAll(outTradeNo,orderMap);
                    }
                }
                return ResponseEntity.ok().build(); // 返回 200 OK，这里需要返回一个200或者204
            } else {
                //验证签名失败
                result.put("code","SUCCESS");
                System.out.println("错误" + result);
                return ResponseEntity.status(400).body(result); // 返回 400 Bad Request
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public String queryOrder(String outTradeNo) {
        String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/" + outTradeNo + "?mchid=" + mchId;
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept","application/json");
            // 加载商户私钥（privateKey：私钥字符串）
            PrivateKey merchantPrivateKey = PemUtil
                    .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
            // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(mchId, new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
                    apiV3Key.getBytes("utf-8"));
            // 初始化httpClient，会自己帮你生成签名并加到请求中
            httpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant(mchId, merchantSerialNumber, merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(verifier)).build();
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String bodyAsString = EntityUtils.toString(response.getEntity());
            System.out.println("订单信息" + bodyAsString);
            return bodyAsString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
