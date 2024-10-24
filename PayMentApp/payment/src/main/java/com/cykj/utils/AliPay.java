package com.cykj.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;

public class AliPay {
    private static String APPID="2021004160619343";
    private static String GateWay = "https://openapi.alipay.com/gateway.do";
    private static String PrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCiOEsnHndgtLN1tNhETbduMrtEXZmLevxtiDuSigizExx/AFdTHDcXqGPFtCp+caZk8QuW9QRDPapBPjKJCsm0VJZIPAJ6n9Js0/IXqAGllPKEbsytCPNxbVmLIg2/VueNMYo1N+tHYbyX1CnOn9fhscQFvChodmUbi2LqAPv+XeW1f4vTrTnEoC7amDR078C2CMv/I7Pw2lOcbVBnjn/5yTrjrxUggrhujnvOzExSz9/vYPU25YeGcqs/8yK48M/ddYCGXO2/a1RfibymqftwNOYtPek8qQyNNWJr7we8/NkNEc3WrknghT7cUWZ8JXiGGV7xqRDNC52hMT8uUnMJAgMBAAECggEAJfNNgaVbAEDGCy93uOMFmr47mU7ccQkgrO9GII4yYtKBxCRsSYE8UkiOrt3rYxFrgAn0UCiaCrEOd5z5bZ4cehef4N5JJi7lp5gd9vLTD828vsa9E24YpdyTKj2ZQb4vBZUlZDXk0RpIaKy16yTx+FcCKOVP/sBT/xGo3l3Elkk41LmtPaoPfrB+sfuMk3swhrVti9zXwRd6/+bzwpjP2hemlptpwKzikFdNadLkSPOO1Je9N5f5xuZZkoWuYvu0K3uTGXblrmC715qzmlNiwCNvvweeUU2yIQsIuD76/9Eu/PmL/3cLANI2x3FyEBP2XDJTHGt+OmN9UsaRFCsxQQKBgQDtMhiYdU/U7tZggOcTDgkeeDO/BnVco9gsnpnwwxoRrzcLosqVS7tGsOyi4e3fp3J64qvQTkLvQEPBwzT4tW3Gl4z9tlPH37GyJTN60xLvhBIAznzNpQ6Y+XPgAiVBfddgaU2/pV9tefisnSswp8CLzKiJJSGTcXATc4tD+mopJQKBgQCvFI7skTPVBvHbIeLYWgPA3Z/Q6DYPvPVk6ihEbwO3xt9hcNAwjUV8m1auXLv+RVCTMqrUGxt1fwSgii4FAHiyupeFMx3MwcN7N8GqiJqAaMs0aAxsML+6mCT+ptZ6D6ghHGB1KdgAzXMhrjby8vMTl7OAZcPjlFlbdJRZ/yPXFQKBgQCIi0ErasT1IDSawznCd176glnPisk+9jb4oKift8CnE4WNlj4mqbz6Wdkvfv3vtTYq1Zfwx0yy6/zVaylwTwkNvKp2V2JtejGSCuZSu3V4GkA+5J9lxrHbkCaLetC1ndrBbn1KaMnBfOiRfbyiOr+V+s++e3iTp0bMe8Rc8qfwXQKBgGl6TjzxobN2kqD9QkgrutUn8A+Qxnkm0wRwGcBYzXmbg5qVt6d4hON0Rn2Xet7ObaOww8AzcJCR7Hy3os/6n7y1s3FZAVwJZDbIuZF+6O3xtHKnOMy5ddj5rlgItXAmHJmOGgKcKfkZzYH+DjbB7XXhUBBLvv9NsaPqkTHDB5mdAoGAZAn5W131ZUBPtCaxyBabmBIyXFWQyNReDjRZ7bOwm9NTng2Ie5JywY/gLRSGe2+j2sGd40Cf92IaGWBm06EoJaqkG8DQwz/xXnzj3ryE+gyUz9XyseQx4SxyEGF/dtah85H/95InN69sPkGI/onqlg+0BzrkIsLz9yoyt3yC1pk=";
    private static String PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAojhLJx53YLSzdbTYRE23bjK7RF2Zi3r8bYg7kooIsxMcfwBXUxw3F6hjxbQqfnGmZPELlvUEQz2qQT4yiQrJtFSWSDwCep/SbNPyF6gBpZTyhG7MrQjzcW1ZiyINv1bnjTGKNTfrR2G8l9Qpzp/X4bHEBbwoaHZlG4ti6gD7/l3ltX+L0605xKAu2pg0dO/AtgjL/yOz8NpTnG1QZ45/+ck6468VIIK4bo57zsxMUs/f72D1NuWHhnKrP/MiuPDP3XWAhlztv2tUX4m8pqn7cDTmLT3pPKkMjTVia+8HvPzZDRHN1q5J4IU+3FFmfCV4hhle8akQzQudoTE/LlJzCQIDAQAB";
    private static String CHARSET = "UTF-8";
    private static String SignType = "RSA2";
    private static String noctityUrl = "http://98x6ct.natappfree.cc";
    private static String returnUrl = "http://98x6ct.natappfree.cc";

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
