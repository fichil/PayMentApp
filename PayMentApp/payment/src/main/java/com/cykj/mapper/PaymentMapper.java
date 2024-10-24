package com.cykj.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    public int addAlipayOrder(String outTradeNum,String merchantNumber,String tradeNum,String price);
}
