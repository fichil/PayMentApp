package com.cykj.service;


import com.cykj.pojo.Admin;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.ChangePasswordVo;
import com.cykj.vo.GetAdminVo;

import javax.servlet.http.HttpSession;

/**
 * @Author: fichil
 * @Date: 2024-09-26 15:34
 * @Description: 管理员服务类接口
 */
public interface AdminService {

    //管理员登陆
    ResponseDTO login(String username, String password, String code, HttpSession session);

    //获取当前管理员信息
    ResponseDTO getAdminInfo(String token);

    //添加管理员
    ResponseDTO addAdmin(Admin admin);

    //修改管理员信息
    ResponseDTO updateAdmin(Admin admin);

    //获取所有管理员
    ResponseDTO selectAllAdmin(GetAdminVo vo);

    //修改管理员密码
    ResponseDTO changePassword(ChangePasswordVo changePasswordVo);
}
