package com.cykj.service;


import com.cykj.pojo.CheckOut;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetCheckOutListVo;

/**
 * @Author: fichil
 * @Date: 2024-10-12 15:13
 * @Description: 商户认证店铺审核服务接口
 */
public interface CheckOutService {

    //模糊搜索legalPerson获取未审核表单并分页
    ResponseDTO getCheckOutList(GetCheckOutListVo getCheckOutListVo);

    //修改商铺认证店铺审核表单信息
    ResponseDTO updateCheckOut(CheckOut checkOut);

}
