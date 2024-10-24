package com.cykj.feign;


import com.cykj.feign.Impl.MerchantServiceFeignClientImpl;
import com.cykj.pojo.MerchantInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: fichil
 * @Date: 2024-10-18 11:05
 * @Description: 商户服务接口
 */
@FeignClient(value = "PayMentApp-merchant",fallbackFactory = MerchantServiceFeignClientImpl.class)
public interface MerchantServiceFeignClient {

    //根据商户编号获取商户信息
    @RequestMapping("/merchant/getMerchantInfoByMerchantNumber")
    MerchantInfo getMerchantInfoByMerchantNumber(String merchantNumber);
}
