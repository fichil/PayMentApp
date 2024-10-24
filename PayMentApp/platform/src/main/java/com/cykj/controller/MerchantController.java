package com.cykj.controller;


import com.cykj.annotation.Logable;
import com.cykj.pojo.MerchantInfo;
import com.cykj.service.MerchantService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetMerchantInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: fichil
 * @Date: 2024-10-08 22:31
 * @Description: 商户管理控制类
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    /**
     * 根据信息模糊查询所有商户
     * @param merchantInfoVo
     * @return
     */
    @RequestMapping("/getMerchantsInfo")
    @ResponseBody
    public ResponseDTO getMerchantsInfo(@RequestBody GetMerchantInfoVo merchantInfoVo) {
        System.out.println(merchantInfoVo.toString());
        return merchantService.getMerchantsInfo(merchantInfoVo);
    }

    /**
     * 添加商户
     * @param merchantInfo
     * @return
     */
    @RequestMapping("/addMerchant")
    @ResponseBody
    public ResponseDTO addMerchant(@RequestBody MerchantInfo merchantInfo) {
        return merchantService.addMerchant(merchantInfo);
    }

    /**
     * 修改商户
     * @param merchantInfo
     * @return
     */
    @Logable("修改商户信息")
    @RequestMapping("/updateMerchant")
    @ResponseBody
    public ResponseDTO updateMerchant(@RequestBody MerchantInfo merchantInfo) {
        return merchantService.updateMerchant(merchantInfo);
    }

    /*
    *  刷新二维码
    *  merchantNumber：商户编号
    * */
    @RequestMapping("/refreshQR")
    @ResponseBody
    public ResponseDTO refreshQR(String merchantNumber){
        ResponseDTO dto = merchantService.refreshQR(merchantNumber);
        return dto;
    }
}
