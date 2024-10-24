package com.cykj.mapper;

import com.cykj.pojo.MainDbOrder;

/**
 * @author 李璟瑜
 * @date 2024/10/10 14:16
 * @description:
 */
public interface MainDbOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MainDbOrder record);

    int insertSelective(MainDbOrder record);

    MainDbOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MainDbOrder record);

    int updateByPrimaryKey(MainDbOrder record);
}