package com.cykj.service;

import com.alipay.api.response.AlipayTradeQueryResponse;

import java.util.HashMap;
import java.util.List;

public interface AlipayService {
    public String createOrder(String merchantNumber,String price);
    public AlipayTradeQueryResponse getOrder(String outTradeNo);
    public boolean callBack(String outTradeNo);
    public String callBack2(String outTradeNo);
}
