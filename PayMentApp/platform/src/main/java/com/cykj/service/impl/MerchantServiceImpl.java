package com.cykj.service.impl;


import com.cykj.feign.PaymentFeignClient;
import com.cykj.mapper.PlatformMerchantAdminMapper;
import com.cykj.mapper.PlatformMerchantMapper;
import com.cykj.pojo.Admin;
import com.cykj.pojo.MerchantInfo;
import com.cykj.service.MerchantService;
import com.cykj.utils.ResponseDTO;
import com.cykj.utils.SnowflakeIdGenerator;
import com.cykj.vo.GetMerchantInfoVo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-08 22:35
 * @Description: 商户管理接口实现类
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private PlatformMerchantMapper merchantMapper;

    @Autowired
    private PlatformMerchantAdminMapper merchantAdminMapper;
    
    @Autowired
    private PaymentFeignClient paymentFeignClient;

    /**
     * 根据条件模糊搜索商户列表
     * @param getMerchantInfoVo
     * @return
     */
    @Override
    public ResponseDTO getMerchantsInfo(GetMerchantInfoVo getMerchantInfoVo) {
        try {
            // 启动分页拦截器
            PageHelper.startPage(getMerchantInfoVo.getCurrentPage(), getMerchantInfoVo.getPageSize());

            // 获取查询结果
            List<MerchantInfo> merchants = merchantMapper.getMerchantsInfo(getMerchantInfoVo);

            // 使用查询结果创建 PageInfo
            PageInfo<MerchantInfo> pageInfo = new PageInfo<>(merchants);
            // 返回包含分页信息和查询结果的 ResponseDTO
            return new ResponseDTO(200,"查询商户成功",pageInfo);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseDTO.default_fail("获取商户信息失败：" + e.getMessage());
        }
    }

    /**
     * 添加商户
     * @param merchantInfo
     * @return
     */
    @Override
    public ResponseDTO addMerchant(MerchantInfo merchantInfo) {
        if (merchantInfo.getLegalPerson() != "" && merchantInfo.getLegalPerson() != null) {
            if (merchantInfo.getTele() != "" && merchantInfo.getTele() != null) {
                //使用雪花算法生成唯一店铺编号
                SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1, 1);
                long l = snowflakeIdGenerator.nextId();
                merchantInfo.setMerchantNumber(l+"");
                int i = merchantMapper.addMerchant(merchantInfo);
                if (i > 0) {

                    //获取店铺信息
                    MerchantInfo merchantInfoByLegalPersonAndTele = merchantMapper.getMerchantInfoByLegalPersonAndTele(merchantInfo);
                    Integer id = merchantInfoByLegalPersonAndTele.getId();

                    //新增商户后创建其账号的管理员
                    Admin admin = new Admin();
                    admin.setNickname(merchantInfo.getLegalPerson());
                    admin.setStoreNumber(l+"");
                    admin.setAccount("merchant"+id);
                    admin.setPassword("e10adc3949ba59abbe56e057f20f883e");
                    admin.setRoleId(1);
                    admin.setState(1);
                    merchantAdminMapper.insertSelective(admin);

                    return new ResponseDTO(200,"添加商户成功","插入数据"+i+";生成商户默认管理员：账号为："+admin.getAccount()+";默认密码为"+admin.getPassword());
                }
            }
        }
        return ResponseDTO.default_fail("添加商户失败");
    }

    /**
     * 修改商户
     * @param merchantInfo
     * @return
     */
    @Override
    public ResponseDTO updateMerchant(MerchantInfo merchantInfo) {
        int i = merchantMapper.updateMerchant(merchantInfo);
        if (i >= 1) {
            return new ResponseDTO(200,"修改商户信息成功","修改条数"+i);
        }
        return ResponseDTO.default_fail("修改商户信息失败");
    }

    @Override
    public ResponseDTO refreshQR(String merchantNumber) {
        ResponseDTO dto = paymentFeignClient.getQrCode(merchantNumber);
        return dto;
    }


}
