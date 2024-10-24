package com.cykj.mapper;


import com.cykj.pojo.CheckOut;
import com.cykj.vo.GetCheckOutListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-12 15:15
 * @Description: 商铺认证店铺审核Mapper
 */
@Mapper
public interface PlatformCheckOutMapper {

    //模糊搜索legalPerson获取未审核表单
    List<CheckOut> getCheckOutList(GetCheckOutListVo getCheckOutListVo);

    //修改商铺认证店铺审核表单信息
    int updateCheckOut(CheckOut checkOut);

    int updateMerchantInfoWithCheckOut(CheckOut checkOut);

    String getMerchantNumber(CheckOut checkOut);

    CheckOut getCheckOutById(int id);

}
