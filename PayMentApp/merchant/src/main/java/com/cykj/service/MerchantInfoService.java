package com.cykj.service;


import com.cykj.pojo.MerchantInfo;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.MerchantAddWithdrawOrderVo;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: fichil
 * @Date: 2024-10-16 10:02
 * @Description: 商户信息服务接口
 */
public interface MerchantInfoService {
    //根据提现账户获取商户信息
    MerchantInfo getMerchantInfoByWithdrawAccount(String withdrawAccount);

    //根据商户编号获取商户信息
    MerchantInfo getMerchantInfoByMerchantNumber(String merchantNumber);

    //商户申请提现
    ResponseDTO MerchantAddWithdrawOrder(MerchantAddWithdrawOrderVo merchantAddWithdrawOrderVo);
}
