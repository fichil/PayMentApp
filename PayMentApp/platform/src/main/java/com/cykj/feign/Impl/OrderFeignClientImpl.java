package com.cykj.feign.Impl;

import com.cykj.feign.OrderFeignClient;
import com.cykj.utils.ResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderFeignClientImpl implements FallbackFactory<OrderFeignClient> {
    @Override
    public OrderFeignClient create(Throwable cause) {
        cause.printStackTrace();
        System.out.println("OrderFeignClient 发生熔断");
        return new OrderFeignClient() {
            @Override
            public ResponseDTO createDb(String merchantNumber) {
                return null;
            }
        };
    }
}
