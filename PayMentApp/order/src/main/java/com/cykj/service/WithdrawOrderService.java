package com.cykj.service;


import com.cykj.pojo.WithdrawOrder;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetWithdrawOrder;
import com.cykj.vo.GetWithdrawOrdersByTwoAccountVo;
import com.cykj.vo.NewWithDrawOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-10 15:15
 * @Description: 提现订单服务接口
 */
public interface WithdrawOrderService {

    //根据提现账号搜索提现订单并分页
    ResponseDTO getWithdrawOrders(GetWithdrawOrder getWithdrawOrder);

    //新增提现订单
    ResponseDTO addWithdrawOrder(WithdrawOrder withdrawOrder);

    //根据提现账户获取提现记录
    ResponseDTO getWithdrawOrdersByTwoAccount(GetWithdrawOrdersByTwoAccountVo getWithdrawOrdersByTwoAccountVo);
}
