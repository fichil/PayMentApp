package com.cykj.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cykj.feign.OrderFeignClient;
import com.cykj.pojo.MainDbOrder;
import com.cykj.service.WeChatPayService;
import com.cykj.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.net.URLEncoder;

@Controller
@RequestMapping("/wxPay")
public class WeChatPayController {
    @Autowired
    private WeChatPayService weChatPayService;

    @Value("${appId}")
    String appId;
    @Value("${appSecret}")
    String appSecret = "f4cf4ea6c6020bd2a3ab57324b04f643";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private OrderFeignClient orderFeign;

    /**
     * 调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易会话标识后生成交易串调起支付
     */
    @RequestMapping("/createOrder")
    @ResponseBody
    public ModelAndView createOrder(String state,String openId) {
        ModelAndView order = weChatPayService.createOrder(state,openId);
        return order;
    }

    @RequestMapping("/test")
    @ResponseBody
    public void test() {
//        Map<String, Object> orderMap = new HashMap<>();
//        orderMap.put("outTradeNo","123hhhhhhh");//预生产订单号
//        orderMap.put("time", TimeUtil.getCurrentTime());
//        redisTemplate.opsForHash().putAll("123hhhhhhh",orderMap);
//        String value = (String)redisTemplate.opsForHash().get("123hhhhhhh", "time");
//        long number = Long.parseLong(value);
//        System.out.println("时间" + number);
//        System.out.println(number);
        long currentTime = TimeUtil.getCurrentTime();
//        System.out.println("当前时间" + currentTime);
//        System.out.println(currentTime);
//        long a = currentTime - number;
//        System.out.println("间隔" + a);
        Date date = new Date(currentTime);
        List<MainDbOrder> orders = new ArrayList<>();
        MainDbOrder mainDbOrder = new MainDbOrder();
        mainDbOrder.setOrderNumber("666");
        mainDbOrder.setTime(date);
        mainDbOrder.setCustomerAccount("888");
        orders.add(mainDbOrder);
        MainDbOrder mainDbOrder1 = new MainDbOrder();
        mainDbOrder1.setOrderNumber("666888");
        mainDbOrder1.setTime(date);
        mainDbOrder1.setCustomerAccount("888999");
        orders.add(mainDbOrder1);
        orderFeign.saveOrder(orders);
    }

    /**
     * 跳转到获取授权码的请求地址
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getWXOAuth2Code")
    public String getWXOAuth2Code(HttpServletRequest request, HttpServletResponse response,String total,String merchantId) {
        System.out.println("执行了");
        String wxOAuth2CodeReturnUrl = "http://ccj.nat300.top/wxPay/wx-oauth-code-return";
        String encode = null;
        String state = total + "=" + merchantId;
        try {
            encode = URLEncoder.encode(wxOAuth2CodeReturnUrl, "UTF-8");
            String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect", appId, encode,state);
            System.out.println("执行了2");
            return "redirect:" + url;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=123#wechat_redirect", appID, wxOAuth2CodeReturnUrl);
        //return "redirect" + url;

    }




    /**
     * 授权码回调，获取授权码和state
     * @param code
     * @param state
     * @return
     */
    @RequestMapping("/wx-oauth-code-return")
    public String wxOAuth2CodeReturn(String code, String state) {
        System.out.println("state" + state);
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",appId,appSecret,code);
        //申请openId,请求url
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String body = exchange.getBody();
        System.out.println("body" + body);
        //return body;
        //获取openId
        Object openid = JSON.parseObject(body).getString("openid");//转为json对象获取openid的值
        System.out.println("openid" + openid);
        return "redirect:http://ccj.nat300.top/wxPay/createOrder?state=" + state +  "&openId=" + openid;
    }

    /**
     * 微信支付成功后回调，成功需返回应答状态码200或者204，
     * 否则微信会认为通知失败，微信会通过一定的策略定期重新发起通知重复调用这个回调函数
     */
    @RequestMapping("/vxCallBack")
    public  ResponseEntity<Map> vxCallBack(HttpServletRequest request) {
        System.out.println("回调了");
        ResponseEntity<Map> mapResponseEntity = weChatPayService.vxCallBack(request);
        return null; // 返回302重定向
    }



    /**
     * 用户未支付，关闭订单接口。订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟
     * 5分钟后调用才有效，不会会报401签名错误
     * @param orderNumber
     * @return
     */
    @RequestMapping("/closeOrder")
    public int closeOrder(String orderNumber) {
        int i = weChatPayService.closeOrder(orderNumber);
        return i;
//        try {
//            // 加载商户私钥（privateKey：私钥字符串）
//            merchantPrivateKey = PemUtil
//                    .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));
//            // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
//            verifier = new AutoUpdateCertificatesVerifier(
//                    new WechatPay2Credentials("1611845480", new PrivateKeySigner("6C73136A86F7E686E6526C0E698014C6245D01D0", merchantPrivateKey)),
//                    "e10adc3949ba59abbe56e057f20f883e".getBytes("utf-8"));
//            // 初始化httpClient
//            httpClient = WechatPayHttpClientBuilder.create()
//                    .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
//                    .withValidator(new WechatPay2Validator(verifier)).build();
//
//            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/"+ orderNumber + "/close");
//            httpPost.addHeader("Accept","application/json");
//            httpPost.addHeader("Content-type","application/json");
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectMapper objectMapper = new ObjectMapper();
//            ObjectNode rootNode = objectMapper.createObjectNode();
//            rootNode.put("mchid",mchid);
//
//            objectMapper.writeValue(bos,rootNode);
//            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"),"UTF-8"));
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            System.out.println(response.getStatusLine().getStatusCode());
//
//            return response.getStatusLine().getStatusCode();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return 0;
    }


    @RequestMapping("/query")
    public void query() {
        String s = weChatPayService.queryOrder("diydpbhde17idccdbl22z2cnmr47seee");
        JSONObject jsonObject = JSON.parseObject(s);

        String tradeState = (String)jsonObject.get("trade_state");
        System.out.println("支付状态" + tradeState);
        if(tradeState !=null) {
            if(tradeState.equals("SUCCESS")) {
                System.out.println("支付成功");
            } else if (tradeState.equals("NOTPAY")){
                System.out.println("订单未支付");
            }
        }

    }



}

