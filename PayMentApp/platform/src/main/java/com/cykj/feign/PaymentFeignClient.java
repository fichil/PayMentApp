package com.cykj.feign;

import com.cykj.feign.Impl.PaymentFeignClientImpl;
import com.cykj.utils.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 李璟瑜
 * @date 2024/10/16 16:12
 * @description:
 */
@FeignClient(value = "PayMentApp-payment",fallbackFactory = PaymentFeignClientImpl.class)
public interface PaymentFeignClient {
    @RequestMapping("/getQrCode")
    ResponseDTO getQrCode(@RequestParam("merchantNumber") String merchantNumber);
}
