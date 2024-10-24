package com.cykj.service.Impl;


import com.cykj.feign.OrderServiceFeignClient;
import com.cykj.feign.PlatformServiceFeignClient;
import com.cykj.mapper.MerchantMerchantInfoMapper;
import com.cykj.pojo.MerchantInfo;
import com.cykj.pojo.WithdrawOrder;
import com.cykj.service.MerchantInfoService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.MerchantAddWithdrawOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: fichil
 * @Date: 2024-10-16 10:02
 * @Description: 商户信息接口实现类
 */
@Service
public class MerchantInfoServiceImpl implements MerchantInfoService {

    @Autowired
    private MerchantMerchantInfoMapper merchantMerchantInfoMapper;

    @Autowired
    private OrderServiceFeignClient orderServiceFeignClient;

    @Autowired
    private PlatformServiceFeignClient platformServiceFeignClient;

    /**
     * 根据提现账户获取商户信息
     * @param withdrawAccount
     * @return
     */
    @Override
    public MerchantInfo getMerchantInfoByWithdrawAccount(String withdrawAccount) {

        return merchantMerchantInfoMapper.getMerchantInfoByWithdrawAccount(withdrawAccount);
    }

    /**
     * 根据商户编号获取商户信息
     * @param merchantNumber
     * @return
     */
    @Override
    public MerchantInfo getMerchantInfoByMerchantNumber(String merchantNumber) {
        return merchantMerchantInfoMapper.getMerchantInfoByMerchantNumber(merchantNumber);
    }

    /**
     * 商户申请提现
     * @param merchantAddWithdrawOrderVo
     * @return
     */
    @Override
    public ResponseDTO MerchantAddWithdrawOrder(MerchantAddWithdrawOrderVo merchantAddWithdrawOrderVo) {
        System.out.println("商户提现："+merchantAddWithdrawOrderVo.toString());
        if (merchantAddWithdrawOrderVo.getMerchantNumber() == "" || merchantAddWithdrawOrderVo.getMerchantNumber() == null){
            if(merchantAddWithdrawOrderVo.getAmount() == "" || merchantAddWithdrawOrderVo.getAmount() == null){
                if(Integer.valueOf(merchantAddWithdrawOrderVo.getAmount()) <= 0){
                    return ResponseDTO.default_fail("参数不合法");
                }
            }
        }
        //获取商户信息
        MerchantInfo merchantInfo = getMerchantInfoByMerchantNumber(merchantAddWithdrawOrderVo.getMerchantNumber());
        if(merchantInfo == null){
            return ResponseDTO.default_fail("没有查找到该商户");
        }
        //判断余额是否足够
        if (merchantInfo.getWallet().compareTo(BigDecimal.valueOf(Long.valueOf(merchantAddWithdrawOrderVo.getAmount()))) < 0){
            return ResponseDTO.default_fail("余额不足");
        }
        WithdrawOrder withdrawOrder = new WithdrawOrder();
        withdrawOrder.setPrice(BigDecimal.valueOf(Long.valueOf(merchantAddWithdrawOrderVo.getAmount())));
        //默认提现到支付宝
        withdrawOrder.setWithdrawAccount(Integer.valueOf(merchantInfo.getAlipayAccount()));
        ResponseDTO dto = orderServiceFeignClient.addWithdrawOrder(withdrawOrder);
        if (dto.getCode() == 200){
            //减少商户余额
            merchantInfo.setWallet(merchantInfo.getWallet().subtract(BigDecimal.valueOf( Integer.valueOf(merchantAddWithdrawOrderVo.getAmount())*(1.001))));
            platformServiceFeignClient.updateMerchant(merchantInfo);
            return dto;
        }
        return ResponseDTO.default_fail("提现失败");
    }
}
