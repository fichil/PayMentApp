package com.cykj.feign;

import com.cykj.feign.Impl.OrderFeignClientImpl;
import com.cykj.utils.ResponseDTO;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 李璟瑜
 * @date 2024/10/16 16:06
 * @description:
 */
@FeignClient(value = "PayMentApp-order",fallbackFactory = OrderFeignClientImpl.class)
public interface OrderFeignClient {

    @RequestMapping("/db/createDb")
    ResponseDTO createDb(@RequestParam("merchantNumber") String merchantNumber);
}
