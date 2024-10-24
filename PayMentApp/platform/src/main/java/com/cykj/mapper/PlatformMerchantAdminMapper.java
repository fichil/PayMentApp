package com.cykj.mapper;


import com.cykj.pojo.Admin;
import com.cykj.pojo.MerchantInfo;
import com.cykj.vo.GetMerchantAdminVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-11 11:32
 * @Description: 商户端管理员Mapper
 */
@Mapper
public interface PlatformMerchantAdminMapper {

    //根据条件查找商户端管理员列表
    List<Admin> getMerchantAdmin(GetMerchantAdminVo getMerchantAdminVo);

    //修改商户管理员信息
    int updateByPrimaryKeySelective(Admin admin);

    //添加商户管理员
    int insertSelective(Admin admin);
}
