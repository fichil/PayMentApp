package com.cykj.mapper;

import com.cykj.pojo.WithdrawOrder;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:48
 * @description:
 */
public interface WithdrawOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WithdrawOrder record);

    int insertSelective(WithdrawOrder record);

    WithdrawOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithdrawOrder record);

    int updateByPrimaryKey(WithdrawOrder record);
}