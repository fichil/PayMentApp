package com.cykj.controller;


import com.cykj.annotation.Logable;
import com.cykj.pojo.Admin;
import com.cykj.service.AdminService;
import com.cykj.service.MenuService;
import com.cykj.utils.ImageCodeUtils;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.ChangePasswordVo;
import com.cykj.vo.GetAdminVo;
import com.cykj.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: fichil
 * @Date: 2024-09-26 14:36
 * @Description: 管理员控制类
 */
@Controller
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    //自动注入验证码生成器
    @Autowired
    private ImageCodeUtils imageCodeUtils;

    //自动注入管理员服务类接口
    @Autowired
    private AdminService adminService;

    //自动注入菜单服务类
    @Autowired
    private MenuService menuService;

    //自动注入RedisTemplate
    @Autowired
    private RedisTemplate redisTemplate;



     /**
     *管理员登陆
     * @param loginVo
     * @param session
     * @return
     */

    @RequestMapping("/login")
    @ResponseBody
    @Logable("管理员登录")
    public ResponseDTO login(@RequestBody LoginVo loginVo, HttpSession session){
        Object o = redisTemplate.opsForValue().get(session.getId());
        if (o == null){
            return new ResponseDTO(-200,"系统错误Redis内没数据",null);
        }
        System.out.println("通过Redis取"+o);
        return adminService.login(loginVo.getUsername(),loginVo.getPassword(),o.toString(),session);
    }


    /**
     * 生成验证码的方法
     * @return
     */
    @RequestMapping("/code")
    @ResponseBody
    public void createCode(HttpServletResponse response, HttpSession session) throws IOException {
        // 生成验证码的图像
        imageCodeUtils.createImage();
        // 获取验证码字符串
        String code = imageCodeUtils.getCode();
        redisTemplate.opsForValue().set(session.getId(),code,5, TimeUnit.MINUTES);
        System.out.println("生成的验证码是：" + code);

        // 将验证码存储到当前用户的 Session 中
        session.setAttribute("verificationCode", code);  // 存入 session 中，键为 "verificationCode"

        // 将图像输出到响应的输出流
        imageCodeUtils.write(response.getOutputStream());
    }


    /**
     * 获取当前管理员信息及菜单
     * @param request
     * @return
     */
    @RequestMapping("/getAdminInfo")
    @ResponseBody
    public ResponseDTO getAdminInfo(HttpServletRequest request){
        // 从请求头获取token
        String token = request.getHeader("admin_token");
        ResponseDTO adminInfo = adminService.getAdminInfo(token);
        ResponseDTO menuList = menuService.getMenuList(0);
        HashMap map = new HashMap();
        map.put("menuList", menuList);
        map.put("adminInfo", adminInfo);
        return new ResponseDTO(200,"获取管理员信息及菜单成功",map);
    }

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @RequestMapping("/addAdmin")
    @ResponseBody
    public ResponseDTO addAdmin(@RequestBody Admin admin){
        return adminService.addAdmin(admin);
    }

    /**
     * 修改管理员信息
     * @param admin
     * @return
     */
    @RequestMapping("/updateAdmin")
    @ResponseBody
    public ResponseDTO updateAdmin(@RequestBody Admin admin){
        return adminService.updateAdmin(admin);
    }

    /**
     * 查询所有平台管理员
     * @param vo
     * @return
     */
    @RequestMapping("/selectAllAdmin")
    @ResponseBody
    public ResponseDTO selectAllAdmin(@RequestBody GetAdminVo vo){
        return adminService.selectAllAdmin(vo);
    }

    /**
     * 修改管理员密码
     * @param changePasswordVo
     * @return
     */
    @RequestMapping("/changePassword")
    @ResponseBody
    public ResponseDTO changePassword(@RequestBody ChangePasswordVo changePasswordVo){
        return adminService.changePassword(changePasswordVo);
    }

}
