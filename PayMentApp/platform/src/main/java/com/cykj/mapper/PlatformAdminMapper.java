package com.cykj.mapper;


import com.cykj.pojo.Admin;
import com.cykj.vo.GetAdminVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-09-26 15:48
 * @Description: 管理员Mapper
 */
@Mapper
public interface PlatformAdminMapper {


    List<Admin> selectAllAdmin();

    //查找所有管理员信息
    List<Admin> selectAdmin(GetAdminVo vo);

    //管理员登陆
    Admin login(@Param("account") String account, @Param("password") String password);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    //添加管理员
    int insertSelective(Admin record);

    //通过id获取管理信息
    Admin selectByPrimaryKey(Integer id);


    //修改管理员信息
    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);


}
