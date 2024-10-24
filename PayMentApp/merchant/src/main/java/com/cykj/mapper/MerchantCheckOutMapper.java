package com.cykj.mapper;

import com.cykj.pojo.CheckOut;
import com.cykj.vo.GetCheckOutRejectByAdminIdVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MerchantCheckOutMapper {
    CheckOut selectCheckOut(int adminId);

//    根据商户管理员id获取商户被拒绝订单
    List<CheckOut> getCheckOutRejectByAdminId(GetCheckOutRejectByAdminIdVo getCheckOutRejectByAdminIdVo);
}
