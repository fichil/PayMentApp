package com.cykj.mapper;

import com.cykj.pojo.Admin;
import com.cykj.pojo.CheckOut;
import com.cykj.pojo.MerchantInfo;
import com.cykj.vo.LoginVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @className: MerchantMapper
 * @author: 沈楠德
 * @date: 2024/09/26 16:22:11
 * @Version: 1.0
 * @description:
 */
@Mapper
public interface MerchantMerchantMapper {
//     获得所有商户
    List<Admin> getAll();
//    商户登陆
    Admin merchantLogin(LoginVo vo);

    CheckOut getOne(String adminId);

    int insertAdminId(String adminId);

    int updateIDCardFrontMsg(@Param("fileName") String fileName,@Param("adminId") String adminId);
    int rollbackIDCardFrontMsg(String adminId);
    int updateIDCardBackMsg(@Param("fileName") String fileName,@Param("adminId") String adminId);
    int rollbackIDCardBackMsg(String adminId);
    int updateLicenseMsg(@Param("fileName") String fileName,@Param("adminId") String adminId);
    int rollbackLicenseMsg(String adminId);

    int updateMerchantInfo(@Param("legalPersonName") String legalPersonName,@Param("legalPersonPhone") String legalPersonPhone,
                           @Param("qrImg") String qrImg,@Param("adminId") String adminId);
}
