package com.cykj.feign.Impl;

import com.cykj.feign.MerchantServiceFeignClient;
import com.cykj.pojo.MerchantInfo;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MerchantServiceFeignClientImpl implements FallbackFactory<MerchantServiceFeignClient> {
    @Override
    public MerchantServiceFeignClient create(Throwable cause) {
        cause.printStackTrace();
        System.out.println("MerchantServiceFeignClient 发生熔断");
        return new MerchantServiceFeignClient() {
            @Override
            public MerchantInfo getMerchantInfoByMerchantNumber(String merchantNumber) {
                return null;
            }
        };
    }
}
