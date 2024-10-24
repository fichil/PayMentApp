package com.cykj.service.impl;


import com.cykj.feign.OrderFeignClient;
import com.cykj.feign.PaymentFeignClient;
import com.cykj.mapper.CheckOutMapper;
import com.cykj.mapper.PlatformCheckOutMapper;
import com.cykj.pojo.CheckOut;
import com.cykj.pojo.MerchantInfo;
import com.cykj.service.CheckOutService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetCheckOutListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-12 15:14
 * @Description: 商户认证店铺审核服务接口实现类
 */
@Service
public class CheckOutServiceImpl implements CheckOutService {

    @Autowired
    private PlatformCheckOutMapper checkOutMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private PaymentFeignClient paymentFeignClient;

    /**
     * 模糊搜索legalPerson获取未审核表单并分页
     * @param getCheckOutListVo
     * @return
     */
    @Override
    public ResponseDTO getCheckOutList(GetCheckOutListVo getCheckOutListVo) {
        try {
            // 启动分页拦截器
            PageHelper.startPage(getCheckOutListVo.getCurrentPage(), getCheckOutListVo.getPageSize());

            // 获取查询结果
            List<CheckOut> checkOuts = checkOutMapper.getCheckOutList(getCheckOutListVo);

            // 使用查询结果创建 PageInfo
            PageInfo<CheckOut> pageInfo = new PageInfo<>(checkOuts);
            // 返回包含分页信息和查询结果的 ResponseDTO
            return new ResponseDTO(200,"查询未通过审核表单成功",pageInfo);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseDTO.default_fail("查询未通过审核表单失败：" + e.getMessage());
        }
    }

    /**
     * 修改商铺认证店铺审核表单信息
     * @param checkOut
     * @return
     */
    @Override
    public ResponseDTO updateCheckOut(CheckOut checkOut) {
        //改变审核订单状态
        int i = checkOutMapper.updateCheckOut(checkOut);
        System.out.println(checkOut.getState());
        if (checkOut.getState()==2){
            return new ResponseDTO(200,"修改商铺认证店铺审核表单信息成功",null);
        }
        System.out.println("checkout.getid:"+checkOut.getId());
        CheckOut newCheckOut = checkOutMapper.getCheckOutById(checkOut.getId());
        System.out.println("newcheckout"+newCheckOut);
        //复制数据到merchantInfo
        checkOutMapper.updateMerchantInfoWithCheckOut(newCheckOut);

        String merchantNumber = checkOutMapper.getMerchantNumber(checkOut);

        //生成二维码
        paymentFeignClient.getQrCode(merchantNumber);


        System.out.println(merchantNumber);
        //生成数据库
        orderFeignClient.createDb(merchantNumber);

        if(i>0){
            return new ResponseDTO(200,"修改商铺认证店铺审核表单信息成功",null);
        }
        return ResponseDTO.default_fail("修改商铺认证店铺审核表单信息失败");
    }

}
