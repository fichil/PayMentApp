//package com.cykj.utils;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.AlipayConfig;
//import com.alipay.api.request.AlipayTradeAppPayRequest;
//import com.alipay.api.domain.AlipayTradeAppPayModel;
//import com.alipay.api.response.AlipayTradeAppPayResponse;
//import com.alipay.api.domain.GoodsDetail;
//
//import com.alipay.api.FileItem;
//import java.util.Base64;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @className: AlipayTradeAppPay
// * @author: 沈楠德
// * @date: 2024/10/08 13:44:13
// * @Version: 1.0
// * @description: 服务端获取orderStr
// */
//public class AlipayTradeAppPay {
//
//    public static void main(String[] args) throws AlipayApiException {
//        // 初始化SDK,创建客户端
//        AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig());
//
//        // 构造请求参数以调用支付接口
//        // 这就是alipay.trade.app.pay接口,用它获取orderStr
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//        // 请求参数实体类，请求的参数在这个实体类里设置
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//
//
//        // 设置订单绝对超时时间
////        model.setTimeExpire("2016-12-31 10:05:00");
//
//        // 设置业务扩展参数
////        ExtendParams extendParams = new ExtendParams();
////        extendParams.setSysServiceProviderId("2088511833207846");
////        extendParams.setHbFqSellerPercent("100");
////        extendParams.setHbFqNum("3");
////        extendParams.setIndustryRefluxInfo("{\"scene_code\":\"metro_tradeorder\",\"channel\":\"xxxx\",\"scene_data\":{\"asset_name\":\"ALIPAY\"}}");
////        extendParams.setSpecifiedSellerName("XXX的跨境小铺");
////        extendParams.setRoyaltyFreeze("true");
////        extendParams.setCardType("S0JP0000");
////        model.setExtendParams(extendParams);
//
//        // 设置商户订单号(必填)
//        model.setOutTradeNo("70501111111S001111119");
//
//        // 设置外部指定买家
////        ExtUserInfo extUserInfo = new ExtUserInfo();
////        extUserInfo.setCertType("IDENTITY_CARD");
////        extUserInfo.setCertNo("362334768769238881");
////        extUserInfo.setName("李明");
////        extUserInfo.setMobile("16587658765");
////        extUserInfo.setMinAge("18");
////        extUserInfo.setNeedCheckInfo("F");
////        extUserInfo.setIdentityHash("27bfcd1dee4f22c8fe8a2374af9b660419d1361b1c207e9b41a754a113f38fcc");
////        model.setExtUserInfo(extUserInfo);
//
//        // 设置通知参数选项
////        List<String> queryOptions = new ArrayList<String>();
////        queryOptions.add("hyb_amount");
////        queryOptions.add("enterprise_pay_info");
////        model.setQueryOptions(queryOptions);
//
//        // 设置订单总金额（必填）
//        model.setTotalAmount("9.00");
//
//        // 设置订单标题
//        model.setSubject("大乐透");
//
//        // 设置产品码
////        model.setProductCode("QUICK_MSECURITY_PAY");
//
//        // 设置公用回传参数
////        model.setPassbackParams("merchantBizType%3d3C%26merchantBizNo%3d2016010101111");
//
//        // 设置订单包含的商品列表信息
//        List<GoodsDetail> goodsDetail = new ArrayList<GoodsDetail>();
//        GoodsDetail goodsDetail0 = new GoodsDetail();
//        goodsDetail0.setGoodsName("ipad");
//        goodsDetail0.setAlipayGoodsId("20010001");
//        goodsDetail0.setQuantity(1L);
//        goodsDetail0.setPrice("2000");
//        goodsDetail0.setGoodsId("apple-01");
//        goodsDetail0.setGoodsCategory("34543238");
//        goodsDetail0.setCategoriesTree("124868003|126232002|126252004");
//        goodsDetail0.setShowUrl("http://www.alipay.com/xxx.jpg");
//        goodsDetail.add(goodsDetail0);
//        model.setGoodsDetail(goodsDetail);
//
//        // 设置商户的原始订单号
//        model.setMerchantOrderNo("20161008001");
//
//        request.setBizModel(model);  // 添加参数完毕后将AlipayTradeAppPayModel对象添加进AlipayTradeAppPayRequest中
//        // 第三方代调用模式下请设置app_auth_token
//        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");
//
//        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);// 使用sdkExecute方法获取orderStr并在响应的body中获取具体值
//        String orderStr = response.getBody();
//        System.out.println("------------start.-------------");
//        System.out.println(orderStr);
//        System.out.println("------------done.--------------");
//        if (response.isSuccess()) {
//            System.out.println("调用成功");
//        } else {
//            System.out.println("调用失败");
//            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
//            // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
//            // System.out.println(diagnosisUrl);
//        }
//    }
//
//    private static AlipayConfig getAlipayConfig() {
//        String privateKey  = AliPay.PrivateKey;
//        String alipayPublicKey = AliPay.PublicKey;
//        AlipayConfig alipayConfig = new AlipayConfig();
//        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
//        alipayConfig.setAppId(AliPay.APPID);
//        alipayConfig.setPrivateKey(privateKey);
//        alipayConfig.setFormat("json");
//        alipayConfig.setAlipayPublicKey(alipayPublicKey);
//        alipayConfig.setCharset("UTF-8");
//        alipayConfig.setSignType("RSA2");
//        return alipayConfig;
//    }
//}