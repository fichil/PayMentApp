package com.cykj.mapper;

import com.cykj.pojo.Menu;

/**
 * @author 李璟瑜
 * @date 2024/10/8 14:55
 * @description:
 */
public interface MenuMapper {
    int deleteByPrimaryKey(Integer menuId);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer menuId);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
}