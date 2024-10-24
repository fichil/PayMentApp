package com.cykj.mapper;

import com.cykj.pojo.Db123456;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:48
 * @description:
 */
public interface Db123456Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Db123456 record);

    int insertSelective(Db123456 record);

    Db123456 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Db123456 record);

    int updateByPrimaryKey(Db123456 record);
}