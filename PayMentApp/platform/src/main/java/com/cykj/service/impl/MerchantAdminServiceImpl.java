package com.cykj.service.impl;


import com.cykj.mapper.PlatformMerchantAdminMapper;
import com.cykj.pojo.Admin;
import com.cykj.service.MerchantAdminService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetMerchantAdminVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-11 11:28
 * @Description: 商户管理员服务接口实现类
 */
@Service
public class MerchantAdminServiceImpl implements MerchantAdminService {

    @Autowired
    private PlatformMerchantAdminMapper platformMerchantAdminMapper;

    /**
     * 根据条件查找商户端管理员列表并分类
     * @param getMerchantAdminVo
     * @return
     */
    @Override
    public ResponseDTO getMerchantAdmin(GetMerchantAdminVo getMerchantAdminVo) {
        try {
            // 启动分页拦截器
            PageHelper.startPage(getMerchantAdminVo.getCurrentPage(), getMerchantAdminVo.getPageSize());

            // 获取查询结果
            List<Admin> admins = platformMerchantAdminMapper.getMerchantAdmin(getMerchantAdminVo);

            // 使用查询结果创建 PageInfo
            PageInfo<Admin> pageInfo = new PageInfo<>(admins);
            // 返回包含分页信息和查询结果的 ResponseDTO
            return new ResponseDTO(200,"查询商户管理员成功",pageInfo);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseDTO.default_fail("获取商户管理员失败：" + e.getMessage());
        }
    }

    /**
     * 修改商户管理员信息
     * @param admin
     * @return
     */
    @Override
    public ResponseDTO updateMerchantAdmin(Admin admin) {
        System.out.println(admin.toString());
        int i = platformMerchantAdminMapper.updateByPrimaryKeySelective(admin);
        if (i > 0) {
            return new ResponseDTO(200,"修改商户管理员信息成功","影响行数："+i);
        }
        return ResponseDTO.default_fail("修改商户管理员信息失败");
    }


}
