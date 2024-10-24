package com.cykj.service;

import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetCheckOutRejectByAdminIdVo;

public interface SelectCheckOut {
    public ResponseDTO selectCheckOut(int adminId);


    //根据商户管理员id获取商户被拒绝订单并分页
    ResponseDTO getCheckOutRejectByAdminId(GetCheckOutRejectByAdminIdVo getCheckOutRejectByAdminIdVo);
}
