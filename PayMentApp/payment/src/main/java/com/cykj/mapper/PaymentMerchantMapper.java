package com.cykj.mapper;

import com.cykj.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @className: MerchantMapper
 * @author: 沈楠德
 * @date: 2024/09/26 16:22:11
 * @Version: 1.0
 * @description:
 */
@Mapper
public interface PaymentMerchantMapper {
//     获得所有商户
    List<Admin> getAll();
    int updateOuterChainName(@Param("merchantNumber") String merchantNumber,
                             @Param("outerChainName") String outerChainName);
}
