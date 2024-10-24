package com.cykj.controller;


import com.cykj.pojo.Admin;
import com.cykj.pojo.MerchantInfo;
import com.cykj.service.MerchantAdminService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetMerchantAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: fichil
 * @Date: 2024-10-11 11:22
 * @Description: 商户管理员控制类
 */
@Controller
@RequestMapping("/merchantAdmin")
public class MerchantAdminController {

    @Autowired
    private MerchantAdminService merchantAdminService;

    /**
     * 根据条件查找商户端管理员列表并分类
     * @param getMerchantAdminVo
     * @return
     */
    @RequestMapping("/getMerchantAdmin")
    @ResponseBody
    public ResponseDTO getMerchantAdmin(@RequestBody GetMerchantAdminVo getMerchantAdminVo) {
        System.out.println(getMerchantAdminVo.toString());
        return merchantAdminService.getMerchantAdmin(getMerchantAdminVo);
    }

    @RequestMapping("/updateMerchantAdmin")
    @ResponseBody
    public ResponseDTO updateMerchantAdmin(@RequestBody Admin admin) {
        return merchantAdminService.updateMerchantAdmin(admin);
    }
}
