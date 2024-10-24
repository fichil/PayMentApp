package com.cykj.service.Impl;


import com.cykj.feign.OrderServiceFeignClient;
import com.cykj.pojo.MerchantInfo;
import com.cykj.service.MerchantInfoService;
import com.cykj.service.OrderService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetWithdrawOrder;
import com.cykj.vo.GetWithdrawOrdersByTwoAccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: fichil
 * @Date: 2024-10-16 14:45
 * @Description: 订单服务接口实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private OrderServiceFeignClient orderServiceFeignClient;


    //查找提现订单
    @Override
    public ResponseDTO getWithdrawOrders(GetWithdrawOrder getWithdrawOrder, HttpServletResponse response) {
        if (getWithdrawOrder.getMerchantNumber() != null && !getWithdrawOrder.getMerchantNumber().isEmpty()) {
            // 根据商户编号查找商户收款账户
            MerchantInfo merchantInfo = merchantInfoService.getMerchantInfoByMerchantNumber(getWithdrawOrder.getMerchantNumber());
            if (merchantInfo == null) {
                return new ResponseDTO(404, "商户信息未找到", null);
            }

            String alipayAccount = merchantInfo.getAlipayAccount();
            String wechatAccount = merchantInfo.getWechatAccount();

            GetWithdrawOrdersByTwoAccountVo getWithdrawOrdersByTwoAccountVo = new GetWithdrawOrdersByTwoAccountVo();
            getWithdrawOrdersByTwoAccountVo.setAli(alipayAccount);
            getWithdrawOrdersByTwoAccountVo.setWx(wechatAccount);
            getWithdrawOrdersByTwoAccountVo.setStartDateStr(getWithdrawOrder.getStartDateStr());
            getWithdrawOrdersByTwoAccountVo.setEndDateStr(getWithdrawOrder.getEndDateStr());
            getWithdrawOrdersByTwoAccountVo.setCurrentPage(getWithdrawOrder.getCurrentPage());
            getWithdrawOrdersByTwoAccountVo.setPageSize(getWithdrawOrder.getPageSize());
            return orderServiceFeignClient.getWithdrawOrdersByTwoAccount(getWithdrawOrdersByTwoAccountVo,response.getHeader("admin-token"));
        }
        return new ResponseDTO(400, "商户编号不能为空", null);
    }

}
