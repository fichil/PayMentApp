package com.cykj.mapper;


import com.cykj.pojo.Admin;
import com.cykj.pojo.MerchantInfo;
import com.cykj.vo.GetMerchantInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-08 22:48
 * @Description: 商户管理Mapper
 */
@Mapper
public interface PlatformMerchantMapper {

    //根据商户编号、商户名称、商户状态模糊查询商户列表
    List<MerchantInfo> getMerchantsInfo(GetMerchantInfoVo getMerchantInfoVo);

    //修改商户信息
    int updateMerchant(MerchantInfo merchantInfo);

    //添加商户店铺信息:法人、电话、alipay、wepay
    int addMerchant(MerchantInfo merchantInfo);

    //根据法人和联系电话搜索店铺信息
    MerchantInfo getMerchantInfoByLegalPersonAndTele(MerchantInfo merchantInfo);

}
