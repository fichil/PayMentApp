package com.cykj.controller;


import com.cykj.feign.OrderServiceFeignClient;
import com.cykj.pojo.MerchantInfo;
import com.cykj.pojo.WithdrawOrder;
import com.cykj.service.MerchantInfoService;
import com.cykj.service.OrderService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetBillByIdVo;
import com.cykj.vo.GetWithdrawOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-15 14:16
 * @Description: 商户订单管理控制类
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderServiceFeignClient orderServiceFeignClient;

    @Autowired
    OrderServiceFeignClient feignClient;
    @Autowired
    private OrderService orderService;



    /**
     * 根据商户id、时间段获取商户流水并分页
     * @param getBillByIdVo
     * @return
     */
    @RequestMapping("/getBillById")
    @ResponseBody
    public ResponseDTO getBillById(@RequestBody GetBillByIdVo getBillByIdVo,HttpServletResponse response) {
        return orderServiceFeignClient.getBillById(getBillByIdVo, response.getHeader("admin_token"));
    }

    /**
     * 根据商户账户、时间段获取商户提现订单并分页
     * @param getWithdrawOrder
     * @param response
     * @return
     */
    @RequestMapping("/getWithdrawOrders")
    @ResponseBody
    public ResponseDTO getWithdrawOrders(@RequestBody GetWithdrawOrder getWithdrawOrder, HttpServletResponse response) {
        return orderService.getWithdrawOrders(getWithdrawOrder,response);
    }

    @GetMapping("/getInfo")
    @ResponseBody
    public ResponseDTO getInfo(String merchantNumber){
        return feignClient.getInfo(merchantNumber);
    }


}
