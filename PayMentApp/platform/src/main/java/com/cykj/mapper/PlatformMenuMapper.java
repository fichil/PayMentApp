package com.cykj.mapper;

import com.cykj.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
@Mapper
public interface PlatformMenuMapper {
    int deleteByPrimaryKey(Integer menuId);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer menuId);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
    List<Menu> getMenuByRoleId(int roleId);
}