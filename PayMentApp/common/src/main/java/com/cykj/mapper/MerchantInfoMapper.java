package com.cykj.mapper;

import com.cykj.pojo.MerchantInfo;

/**
 * @author 李璟瑜
 * @date 2024/10/11 17:26
 * @description:
 */
public interface MerchantInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantInfo record);

    int insertSelective(MerchantInfo record);

    MerchantInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantInfo record);

    int updateByPrimaryKey(MerchantInfo record);
}