package com.cykj.mapper;

import com.cykj.pojo.Permission;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer permissionId);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer permissionId);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}