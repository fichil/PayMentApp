package com.cykj.feign.Impl;

import com.cykj.feign.OrderFeignClient;
import com.cykj.pojo.MainDbOrder;
import com.cykj.utils.ResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFeignClientImpl implements FallbackFactory<OrderFeignClient> {

    @Override
    public OrderFeignClient create(Throwable cause) {
        cause.printStackTrace();
        System.out.println("发生熔断了");
        return new OrderFeignClient() {
            @Override
            public ResponseDTO saveOrder(List<MainDbOrder> orders) {
                return null;
            }

            @Override
            public ResponseDTO addOrder(List<MainDbOrder> orders) {
                return null;
            }
        };
    }
}
