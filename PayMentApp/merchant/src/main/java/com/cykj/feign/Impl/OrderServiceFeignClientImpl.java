package com.cykj.feign.Impl;

import com.cykj.feign.OrderServiceFeignClient;
import com.cykj.pojo.WithdrawOrder;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetBillByIdVo;
import com.cykj.vo.GetWithdrawOrder;
import com.cykj.vo.GetWithdrawOrdersByTwoAccountVo;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceFeignClientImpl implements FallbackFactory<OrderServiceFeignClient> {
    @Override
    public OrderServiceFeignClient create(Throwable cause) {
        cause.printStackTrace();
        System.out.println("OrderServiceFeignClient 发生熔断");
        return new OrderServiceFeignClient() {
            @Override
            public ResponseDTO getBillById(GetBillByIdVo getBillByIdVo, String token) {
                return null;
            }

            @Override
            public ResponseDTO getWithdrawOrders(GetWithdrawOrder getWithdrawOrder, String token) {
                return null;
            }

            @Override
            public ResponseDTO getInfo(String merchantNumber) {
                return null;
            }

            @Override
            public ResponseDTO getWithdrawOrdersByTwoAccount(GetWithdrawOrdersByTwoAccountVo getWithdrawOrdersByTwoAccountVo, String token) {
                return null;
            }

            @Override
            public ResponseDTO addWithdrawOrder(WithdrawOrder withdrawOrder) {
                return null;
            }
        };
    }
}
