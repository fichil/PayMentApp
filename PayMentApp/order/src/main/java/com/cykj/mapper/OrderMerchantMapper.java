package com.cykj.mapper;


import com.cykj.pojo.Db123456;
import com.cykj.vo.GetBillByIdVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-14 15:49
 * @Description: 商户请求Mapper
 */
@Mapper
public interface OrderMerchantMapper {

    //根据商户id、时间段获取商户流水
    List<Db123456> getBillById(GetBillByIdVo getBillByIdVo);

}
