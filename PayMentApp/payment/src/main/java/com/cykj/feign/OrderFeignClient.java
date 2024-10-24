package com.cykj.feign;

import com.cykj.feign.Impl.OrderFeignClientImpl;
import com.cykj.pojo.MainDbOrder;
import com.cykj.utils.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "PayMentApp-order",fallbackFactory = OrderFeignClientImpl.class)
public interface OrderFeignClient {

    @PostMapping ("/order/saveOrder")
    public ResponseDTO saveOrder(
            @RequestBody List<MainDbOrder> orders
    );

    @PostMapping("/merchant/addOrder")
    ResponseDTO addOrder(@RequestBody List<MainDbOrder> orders);


}
