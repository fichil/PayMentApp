package com.cykj.feign.Impl;

import com.cykj.feign.PlatformServiceFeignClient;
import com.cykj.pojo.Admin;
import com.cykj.pojo.MerchantInfo;
import com.cykj.utils.ResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PlatformServiceFeignClientImpl implements FallbackFactory<PlatformServiceFeignClient> {
    @Override
    public PlatformServiceFeignClient create(Throwable cause) {
        cause.printStackTrace();
        System.out.println("PlatformServiceFeignClient 发生了熔断");
        return new PlatformServiceFeignClient() {
            @Override
            public ResponseDTO updateAdmin(Admin admin, String token) {
                return null;
            }

            @Override
            public ResponseDTO updateMerchant(MerchantInfo merchantInfo) {
                return null;
            }
        };
    }
}
