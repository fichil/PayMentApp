package com.cykj.controller;

import com.cykj.service.SelectCheckOut;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetCheckOutRejectByAdminIdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
public class SelectCheckOutController {
    @Autowired
    SelectCheckOut selectCheckOut;
    @RequestMapping("/selectCheckOut")
    public ResponseDTO selectCheckOut(int adminId) {
        System.out.println("adminId" + adminId);
        ResponseDTO dto = selectCheckOut.selectCheckOut(adminId);
        return dto;
    }

    /**
     * 根据商户管理员id获取商户被拒绝订单并分页
     * @param adminId
     * @return
     */
    @RequestMapping("/getCheckOutRejectByAdminId")
    public ResponseDTO getCheckOutRejectByAdminId(@RequestBody GetCheckOutRejectByAdminIdVo getCheckOutRejectByAdminIdVo) {
        return selectCheckOut.getCheckOutRejectByAdminId(getCheckOutRejectByAdminIdVo);
    }

}
