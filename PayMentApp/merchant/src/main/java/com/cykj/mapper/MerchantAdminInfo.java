package com.cykj.mapper;

import com.cykj.pojo.Admin;
import com.cykj.vo.RegisterVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 李璟瑜
 * @date 2024/10/16 10:47
 * @description:
 */
@Mapper
public interface MerchantAdminInfo {
    Admin getAdmin(RegisterVo vo);
    Integer addAdmin(RegisterVo vo);
}
