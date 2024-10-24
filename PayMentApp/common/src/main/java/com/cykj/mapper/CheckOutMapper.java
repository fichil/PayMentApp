package com.cykj.mapper;

import com.cykj.pojo.CheckOut;


/**
 * @author 李璟瑜
 * @date 2024/10/16 9:41
 * @description:
 */

public interface CheckOutMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CheckOut record);

    int insertSelective(CheckOut record);

    CheckOut selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CheckOut record);

    int updateByPrimaryKey(CheckOut record);


}