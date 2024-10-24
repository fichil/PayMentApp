package com.cykj.mapper;

import com.cykj.pojo.Admin;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
public interface AdminMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

}