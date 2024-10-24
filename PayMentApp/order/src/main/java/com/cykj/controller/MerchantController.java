package com.cykj.controller;


import com.cykj.pojo.MainDbOrder;
import com.cykj.service.MerchantService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetBillByIdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-14 15:41
 * @Description: 商户控制类
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    /**
     * 根据商户id、时间段获取商户流水并分页
     * @param getBillByIdVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBillById")
    public ResponseDTO getBillById(@RequestBody GetBillByIdVo getBillByIdVo) {
        System.out.println(getBillByIdVo.toString());
        return merchantService.getBillById(getBillByIdVo);
    }
    @ResponseBody
    @RequestMapping("/getInfo")
    public ResponseDTO getInfo(@RequestParam("merchantNumber") String merchantNumber){
        return merchantService.getInfo(merchantNumber);
    }

    @ResponseBody
    @RequestMapping("/addOrder")
    public ResponseDTO addOrder(@RequestBody List<MainDbOrder> orders){
        return merchantService.addOrder(orders);
    }
}
