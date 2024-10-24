package com.cykj.feign.Impl;

import com.cykj.feign.PaymentFeignClient;
import com.cykj.utils.ResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentFeignClientImpl implements FallbackFactory<PaymentFeignClient> {
    @Override
    public PaymentFeignClient create(Throwable cause) {
        return new PaymentFeignClient() {
            @Override
            public ResponseDTO getQrCode(String merchantNumber) {
                return null;
            }
        };
    }
}
