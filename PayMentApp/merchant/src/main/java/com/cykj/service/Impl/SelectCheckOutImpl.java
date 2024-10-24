package com.cykj.service.Impl;

import com.cykj.mapper.CheckOutMapper;
import com.cykj.mapper.MerchantCheckOutMapper;
import com.cykj.pojo.CheckOut;
import com.cykj.service.SelectCheckOut;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetCheckOutRejectByAdminIdVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectCheckOutImpl implements SelectCheckOut {
    @Autowired
    private MerchantCheckOutMapper merchantCheckOutMapper;

    @Override
    public ResponseDTO selectCheckOut(int adminId) {

        CheckOut checkOut = merchantCheckOutMapper.selectCheckOut(adminId);
        return new ResponseDTO(200,"查询成功",checkOut);
    }

    /**
     *根据商户管理员id获取商户被拒绝订单并分页
     * @param getCheckOutRejectByAdminIdVo
     * @return
     */
    @Override
    public ResponseDTO getCheckOutRejectByAdminId(GetCheckOutRejectByAdminIdVo getCheckOutRejectByAdminIdVo) {
        try {
            // 启动分页拦截器
            PageHelper.startPage(getCheckOutRejectByAdminIdVo.getCurrentPage(), getCheckOutRejectByAdminIdVo.getPageSize());


            // 获取查询结果
            List<CheckOut> orders = merchantCheckOutMapper.getCheckOutRejectByAdminId(getCheckOutRejectByAdminIdVo);

            // 使用查询结果创建 PageInfo
            PageInfo<CheckOut> pageInfo = new PageInfo<>(orders);
            // 返回包含分页信息和查询结果的 ResponseDTO
            return new ResponseDTO(200,"查询商户被拒绝的审核成功",pageInfo);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseDTO.default_fail("获取商户被拒绝的审核失败：" + e.getMessage());
        }
    }
}
