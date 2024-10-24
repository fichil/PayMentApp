package com.cykj.controller;


import com.cykj.annotation.Logable;
import com.cykj.pojo.CheckOut;
import com.cykj.service.CheckOutService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetCheckOutListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: fichil
 * @Date: 2024-10-12 15:07
 * @Description: 商户认证店铺审核控制类
 */
@Controller
@RequestMapping("/checkOut")
public class CheckOutController {

    @Autowired
    private CheckOutService checkOutService;

    /**
     * 模糊搜索legalPerson获取未审核表单并分页
     * @param getCheckOutListVo
     * @return
     */
    @RequestMapping("/getCheckOutList")
    @ResponseBody
    public ResponseDTO getCheckOutList(@RequestBody GetCheckOutListVo getCheckOutListVo){
        return checkOutService.getCheckOutList(getCheckOutListVo);
    }

    /**
     * 商铺认证店铺审核通过
     * @param checkOut
     * @return
     */
    @RequestMapping("/agree")
    @Logable("审核通过")
    @ResponseBody
    public ResponseDTO agree(@RequestBody CheckOut checkOut){
        return checkOutService.updateCheckOut(checkOut);
    }

    /**
     * 商铺认证店铺审核不通过
     * @param checkOut
     * @return
     */
    @RequestMapping("/reject")
    @Logable("审核拒绝")
    @ResponseBody
    public ResponseDTO reject(@RequestBody CheckOut checkOut){
        return checkOutService.updateCheckOut(checkOut);
    }

}
