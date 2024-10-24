package com.cykj.mapper;

import com.cykj.pojo.AdminRole;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
public interface AdminRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(AdminRole record);

    int insertSelective(AdminRole record);

    AdminRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(AdminRole record);

    int updateByPrimaryKey(AdminRole record);
}