package com.cykj.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;

public class AliPay {
    private static String APPID="2021004160619343";
    private static String GateWay = "https://openapi.alipay.com/gateway.do";
    private static String PrivateKey = "" ;
    private static String PublicKey = "" ;
    private static String CHARSET = "UTF-8";
    private static String SignType = "RSA2";
    private static String noctityUrl = "";
    private static String returnUrl = "";

    private static DefaultAlipayClient aliPayClient;
    private AliPay(){

    }

    /**
     * @description: 获取支付宝客户端
     * @param: 无
     * @return: 客户端对象
     */

    public static DefaultAlipayClient getAlipayClient() {
            if (aliPayClient == null){
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                certAlipayRequest.setServerUrl(GateWay);
                certAlipayRequest.setAppId(APPID);
                certAlipayRequest.setPrivateKey(PrivateKey);
                certAlipayRequest.setFormat("json");
                certAlipayRequest.setCharset(CHARSET);
                certAlipayRequest.setSignType(SignType);
                certAlipayRequest.setCertPath("D:\\alipay\\appCertPublicKey_2021004160619343.crt");
                certAlipayRequest.setAlipayPublicCertPath("D:\\alipay\\alipayCertPublicKey_RSA2.crt");
                certAlipayRequest.setRootCertPath("D:\\alipay\\alipayRootCert.crt");
            try {
                aliPayClient = new DefaultAlipayClient(certAlipayRequest);
                return aliPayClient;
            } catch (AlipayApiException e) {
                e.printStackTrace();
                return null;
            }
        }else {
                return aliPayClient;
            }
    }



    public static String getAPPID() {
        return APPID;
    }

    public static String getGateWay() {
        return GateWay;
    }

    public static String getPrivateKey() {
        return PrivateKey;
    }

    public static String getPublicKey() {
        return PublicKey;
    }

    public static String getCHARSET() {
        return CHARSET;
    }

    public static String getSignType() {
        return SignType;
    }

    public static String getNoctityUrl() {
        return noctityUrl;
    }

    public static String getReturnUrl() {
        return returnUrl;
    }
}
