package com.cykj.service.impl;


import com.alibaba.fastjson.JSON;
import com.cykj.mapper.PlatformAdminMapper;
import com.cykj.mapper.PlatformLogMapper;
import com.cykj.pojo.Admin;
import com.cykj.pojo.Log;
import com.cykj.pojo.MerchantInfo;
import com.cykj.service.AdminService;
import com.cykj.utils.JwtUtils;
import com.cykj.utils.Md5;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.ChangePasswordVo;
import com.cykj.vo.GetAdminVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: fichil
 * @Date: 2024-09-26 15:37
 * @Description: 管理员服务接口实现类
 */
@Service
public class AdminServiceImpl implements AdminService {

//    admin的Mapper
    @Autowired
    private PlatformAdminMapper platformAdminMapper;

//    Redis工具
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    //自动注入日志类
    @Autowired
    private PlatformLogMapper logMapper;

    /**
     * 管理员登录
     * @param account 管理员账号
     * @param password 管理员密码
     * @param code 用户输入的验证码
     * @param session HttpSession，用于存储验证码
     * @return Admin 管理员信息
     */
    @Override
    public ResponseDTO login(String account, String password, String code, HttpSession session) {
        password = Md5.getEncrypted(password);
        // 从 Session 中获取存储的验证码
        String verificationCode = (String) session.getAttribute("verificationCode");
        //清除session中的验证码
        session.removeAttribute("verificationCode");

        // 判断验证码是否与用户输入的匹配
        if (verificationCode == null || !verificationCode.equalsIgnoreCase(code)) {
            // 如果验证码为空或者验证码不匹配，返回 dto
            System.out.println("验证码不正确");
            return new ResponseDTO(-300,"验证码不正确",null);
        }

        // 验证码通过后，继续进行账号密码的验证
        Admin admin = platformAdminMapper.login(account, password);

        if (admin == null || admin.getId() == null) {
            System.out.println("账号或密码错误");
            return new ResponseDTO(-200,"账号或密码错误",null);
        }
        //隐藏密码
        admin.setPassword(null);
        HashMap<String,Object> data = new HashMap<>();
        data.put("id",admin.getId());
        redisTemplate.delete(session.getId());
        String token = JwtUtils.generateToken(data);
        //设置过去时间
        redisTemplate.opsForValue().set(token,JSON.toJSONString(admin),30, TimeUnit.MINUTES);

        logMapper.insertSelective(new Log(admin.getId(), "管理员登录"));

        return new ResponseDTO(200,"登陆成功",token);
    }

    /**
     * 根据管理员id获取该管理员信息
     * @return
     */
    @Override
    public ResponseDTO getAdminInfo(String token) {
        //从redis中获取管理员信息
        String s = (String) redisTemplate.opsForValue().get(token);
        Admin admin = JSON.parseObject(s,Admin.class);
        return ResponseDTO.default_success(admin);
    }

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @Override
    public ResponseDTO addAdmin(Admin admin) {
        admin.setPassword(Md5.getEncrypted(admin.getPassword()));
        int changeRow = platformAdminMapper.insertSelective(admin);
        if (changeRow > 0) {
            return ResponseDTO.default_success("添加成功");
        }
        return ResponseDTO.default_fail("添加失败");
    }

    /**
     * 修改管理员信息
     * @param admin
     * @return
     */
    @Override
    public ResponseDTO updateAdmin(Admin admin) {
        System.out.println(admin.toString());
        if (admin.getId() == null) {
            return ResponseDTO.default_fail("id不能为空");
        }

        if(admin.getNickname() == null && admin.getPassword() == null &&
                admin.getAccount() == null && admin.getState() == null &&
                admin.getRoleId() == null && admin.getStoreNumber() == null){
            return ResponseDTO.default_fail("不能没有要的修改参数");
        }
        if(admin.getPassword() != null){
            //密码设置为md5格式
            admin.setPassword(Md5.getEncrypted(admin.getPassword()));
        }
        int i = platformAdminMapper.updateByPrimaryKeySelective(admin);
        if (i > 0) {
            return new ResponseDTO(200,"修改成功","影响行数"+i);
        }
        return ResponseDTO.default_fail("修改失败");
    }

    /**
     * 查询所有管理员信息
     * @return
     */
    @Override
    public ResponseDTO selectAllAdmin(GetAdminVo vo) {
//      启动分页拦截器
        PageHelper.startPage(vo.getCurrentPage(),vo.getPageSize());

        List<Admin> admins = platformAdminMapper.selectAdmin(vo);
        for (Admin admin : admins) {
            admin.setPassword(null);
        }

        // 使用查询结果创建 PageInfo
        PageInfo<Admin> pageInfo = new PageInfo<>(admins);

        return ResponseDTO.default_success(pageInfo);
    }

    /**
     * 修改管理员密码
     * @param changePasswordVo
     * @return
     */
    @Override
    public ResponseDTO changePassword(ChangePasswordVo changePasswordVo) {
        if (changePasswordVo.getOldPassword() == null || changePasswordVo.getNewPassword() == null || changePasswordVo.getId() == 0) {
            return ResponseDTO.default_fail("id、旧密码、新密码不能为空");
        }
        Admin admin = platformAdminMapper.selectByPrimaryKey(changePasswordVo.getId());
        String password = admin.getPassword();
        if (changePasswordVo.getOldPassword().equals(password) || Md5.getEncrypted(changePasswordVo.getOldPassword()).equals(password)){
            admin.setPassword(changePasswordVo.getNewPassword());
            return updateAdmin(admin);
        }


        return ResponseDTO.default_fail("修改失败,旧密码错误");
    }

}
