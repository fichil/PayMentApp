package com.cykj.service;


import com.cykj.pojo.Admin;
import com.cykj.pojo.MerchantInfo;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetMerchantInfoVo;

/**
 * @Author: fichil
 * @Date: 2024-10-08 22:34
 * @Description: 商户管理服务接口
 */
public interface MerchantService {

    //根据条件模糊搜索商户列表
    ResponseDTO getMerchantsInfo(GetMerchantInfoVo getMerchantInfoVo);

    //添加商户
    ResponseDTO addMerchant(MerchantInfo merchantInfo);

    //修改商户信息
    ResponseDTO updateMerchant(MerchantInfo merchantInfo);

    public ResponseDTO refreshQR(String merchantNumber);
}
