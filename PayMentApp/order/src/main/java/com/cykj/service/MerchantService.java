package com.cykj.service;


import com.cykj.pojo.MainDbOrder;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetBillByIdVo;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-14 15:43
 * @Description: 商户请求服务接口
 */
public interface MerchantService {

    //根据商户id、时间段获取商户流水并分页
    ResponseDTO getBillById(GetBillByIdVo getBillByIdVo);
    ResponseDTO getInfo(String merchantNumber);
    ResponseDTO addOrder(List<MainDbOrder> orders);
}
