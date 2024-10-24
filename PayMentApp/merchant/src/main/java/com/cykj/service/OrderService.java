package com.cykj.service;


import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetBillByIdVo;
import com.cykj.vo.GetWithdrawOrder;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: fichil
 * @Date: 2024-10-16 14:44
 * @Description: 订单服务接口
 */
public interface OrderService {

    //根据商户id、时间段获取商户流水并分页
    ResponseDTO getWithdrawOrders(GetWithdrawOrder getWithdrawOrder, HttpServletResponse response);
}
