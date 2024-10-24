package com.cykj.service;


import com.cykj.pojo.Admin;
import com.cykj.pojo.MerchantInfo;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetMerchantAdminVo;

/**
 * @Author: fichil
 * @Date: 2024-10-11 11:27
 * @Description: 商户管理员服务接口
 */
public interface MerchantAdminService {

    //根据条件查找商户端管理员列表并分类
    ResponseDTO getMerchantAdmin(GetMerchantAdminVo getMerchantAdminVo);

    //修改商户管理员信息
    ResponseDTO updateMerchantAdmin(Admin admin);
}
