package com.cykj.mapper;

import com.cykj.pojo.MerchantInfo;
import com.cykj.pojo.MerchantInfoAndAdmin;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
@Mapper
public interface MerchantMerchantInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantInfo record);

    int insertSelective(MerchantInfo record);

    MerchantInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantInfo record);

    int updateByPrimaryKey(MerchantInfo record);

    MerchantInfoAndAdmin getUserInfo(Integer id);

    //根据提现账户获取商户信息
    MerchantInfo getMerchantInfoByWithdrawAccount(String withdrawAccount);

    //根据商户编号获取商户信息
    MerchantInfo getMerchantInfoByMerchantNumber(String merchantNumber);

}