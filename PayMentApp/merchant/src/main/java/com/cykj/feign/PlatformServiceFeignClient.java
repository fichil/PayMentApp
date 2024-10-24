package com.cykj.feign;


import com.cykj.feign.Impl.PlatformServiceFeignClientImpl;
import com.cykj.pojo.Admin;
import com.cykj.pojo.MerchantInfo;
import com.cykj.utils.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: fichil
 * @Date: 2024-10-16 11:23
 * @Description: 平台模块的feign接口
 */
@FeignClient(value = "PayMentApp-platform",fallbackFactory = PlatformServiceFeignClientImpl.class)
public interface PlatformServiceFeignClient {

    //修改商户管理员信息
    @RequestMapping("/admin/updateAdmin")
    ResponseDTO updateAdmin(Admin admin,@RequestHeader("admin-token") String token);

    //修改商户信息
    @RequestMapping("/merchant/updateMerchant")
    ResponseDTO updateMerchant(MerchantInfo merchantInfo);
}
