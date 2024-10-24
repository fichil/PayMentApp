package com.cykj.feign;


import com.cykj.feign.Impl.OrderServiceFeignClientImpl;
import com.cykj.pojo.WithdrawOrder;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetBillByIdVo;
import com.cykj.vo.GetWithdrawOrder;
import com.cykj.vo.GetWithdrawOrdersByTwoAccountVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: fichil
 * @Date: 2024-10-15 14:21
 * @Description: 调用Order模块接口的接口
 */
@FeignClient(value = "PayMentApp-order",fallbackFactory = OrderServiceFeignClientImpl.class)
public interface OrderServiceFeignClient {

    //根据商户id、时间段获取商户流水并分页
    @RequestMapping("/merchant/getBillById")
    ResponseDTO getBillById(GetBillByIdVo getBillByIdVo,@RequestHeader("admin-token") String token);

    //根据商户账户、时间段获取商户提现订单并分页
    @RequestMapping("/withdrawOrder/getWithdrawOrders")
    ResponseDTO getWithdrawOrders(GetWithdrawOrder getWithdrawOrder,@RequestHeader("admin-token") String token);

    @RequestMapping("/merchant/getInfo")
    ResponseDTO getInfo(@RequestParam("merchantNumber") String merchantNumber);

    //根据提现账户获取提现记录
    @RequestMapping("/withdrawOrder/getWithdrawOrdersByTwoAccount")
    ResponseDTO getWithdrawOrdersByTwoAccount(GetWithdrawOrdersByTwoAccountVo getWithdrawOrdersByTwoAccountVo,@RequestHeader("admin-token") String token);

    //商户发起提现
    @RequestMapping("/withdrawOrder/addWithdrawOrder")
    ResponseDTO addWithdrawOrder(@RequestBody WithdrawOrder withdrawOrder);
}
