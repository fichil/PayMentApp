package com.cykj.controller;


import com.cykj.annotation.Logable;
import com.cykj.feign.OrderServiceFeignClient;
import com.cykj.pojo.MerchantInfo;
import com.cykj.pojo.WithdrawOrder;
import com.cykj.service.MerchantInfoService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.MerchantAddWithdrawOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * @Author: fichil
 * @Date: 2024-10-16 10:01
 * @Description: 商户信息控制类
 */
@Controller
@RequestMapping("/merchantInfo")
public class MerchantInfoController {

    @Autowired
    private MerchantInfoService merchantInfoService;



    /**
     * 根据提现账户获取商户信息
     * @param withdrawAccount
     * @return
     */
    @RequestMapping("/getMerchantInfoByWithdrawAccount")
    @ResponseBody
    public MerchantInfo getMerchantInfoByWithdrawAccount(@RequestParam String withdrawAccount){
        return merchantInfoService.getMerchantInfoByWithdrawAccount(withdrawAccount);
    }

    /**
     * 根据商户编号获取商户信息
     * @param merchantNumber
     * @return
     */
    @RequestMapping("/getMerchantInfoByMerchantNumber")
    @ResponseBody
    public MerchantInfo getMerchantInfoByMerchantNumber(String merchantNumber){
        return merchantInfoService.getMerchantInfoByMerchantNumber(merchantNumber);
    }

    /**
     * 商户发起提现
     * @param merchantAddWithdrawOrderVo
     * @return
     */
    @RequestMapping("/MerchantAddWithdrawOrder")
    @ResponseBody
    @Logable("商户提现")
    public ResponseDTO MerchantAddWithdrawOrder(@RequestBody MerchantAddWithdrawOrderVo merchantAddWithdrawOrderVo) {
        return merchantInfoService.MerchantAddWithdrawOrder(merchantAddWithdrawOrderVo);
    }
}
