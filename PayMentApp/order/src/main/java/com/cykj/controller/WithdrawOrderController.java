package com.cykj.controller;


import com.cykj.pojo.WithdrawOrder;
import com.cykj.service.WithdrawOrderService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetWithdrawOrder;
import com.cykj.vo.GetWithdrawOrdersByTwoAccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: fichil
 * @Date: 2024-10-10 15:10
 * @Description: 提现订单控制类
 */
@Controller
@RequestMapping("/withdrawOrder")
public class WithdrawOrderController {

    @Autowired
    private WithdrawOrderService withdrawOrderService;

    /**
     * 根据提现账号搜索提现订单并分页
     * @param getWithdrawOrder
     * @return
     */
    @RequestMapping("/getWithdrawOrders")
    @ResponseBody
    public ResponseDTO getWithdrawOrders(@RequestBody GetWithdrawOrder getWithdrawOrder){
        return withdrawOrderService.getWithdrawOrders(getWithdrawOrder);
    }

    /**
     * 添加提现订单信息
     * @param withdrawOrder
     * @return
     */
    @RequestMapping("/addWithdrawOrder")
    @ResponseBody
    public ResponseDTO addWithdrawOrder(@RequestBody WithdrawOrder withdrawOrder){
        return withdrawOrderService.addWithdrawOrder(withdrawOrder);
    }

    /**
     * 根据提现账户获取提现记录
     * @param getWithdrawOrdersByTwoAccountVo
     * @return
     */
    @RequestMapping("/getWithdrawOrdersByTwoAccount")
    @ResponseBody
    public ResponseDTO getWithdrawOrdersByTwoAccount(@RequestBody GetWithdrawOrdersByTwoAccountVo getWithdrawOrdersByTwoAccountVo){
        return withdrawOrderService.getWithdrawOrdersByTwoAccount(getWithdrawOrdersByTwoAccountVo);
    }
}
