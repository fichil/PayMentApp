package com.cykj.service.impl;


import com.cykj.feign.MerchantServiceFeignClient;
import com.cykj.mapper.OrderWithdrawOrderMapper;
import com.cykj.pojo.WithdrawOrder;
import com.cykj.service.WithdrawOrderService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetWithdrawOrder;
import com.cykj.vo.GetWithdrawOrdersByTwoAccountVo;
import com.cykj.vo.NewWithDrawOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-10 15:21
 * @Description: 提现订单服务接口实现类
 */
@Service
public class WithdrawOrderServiceImpl implements WithdrawOrderService {

    @Autowired
    private OrderWithdrawOrderMapper withdrawOrderMapper;


    /**
     * 根据提现账号搜索提现订单并分页
     * @param getWithdrawOrder
     * @return
     */
    @Override
    public ResponseDTO getWithdrawOrders(GetWithdrawOrder getWithdrawOrder) {
        try {
            // 启动分页拦截器
            PageHelper.startPage(getWithdrawOrder.getCurrentPage(), getWithdrawOrder.getPageSize());

            //如果需要根据时间查询
            if(getWithdrawOrder.getStartDateStr() != null && getWithdrawOrder.getEndDateStr() != null){
                getWithdrawOrder.setStartTime(convertStringToTimestamp(getWithdrawOrder.getStartDateStr()));
                getWithdrawOrder.setEndTime(convertStringToTimestamp(getWithdrawOrder.getEndDateStr()));
            }


            // 获取查询结果
            List<NewWithDrawOrder> orders = withdrawOrderMapper.getWithdrawOrders(getWithdrawOrder);

            // 使用查询结果创建 PageInfo
            PageInfo<NewWithDrawOrder> pageInfo = new PageInfo<>(orders);
            // 返回包含分页信息和查询结果的 ResponseDTO
            return new ResponseDTO(200,"查询提现订单成功",pageInfo);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseDTO.default_fail("获取提现订单失败：" + e.getMessage());
        }
    }

    /**
     * 新增提现订单
     * @param withdrawOrder
     * @return
     */
    @Override
    public ResponseDTO addWithdrawOrder(WithdrawOrder withdrawOrder) {
        withdrawOrder.setId(null);
        withdrawOrder.setCreateTime(null);
        withdrawOrder.setTax("0.1%");
        int i = withdrawOrderMapper.insertSelective(withdrawOrder);
        if (i > 0) {
            return new ResponseDTO(200, "新增提现信息成功", "插入条数：" + i);
        }

        return ResponseDTO.default_fail("新增提现信息失败");
    }


    /**
     * 据提现账户获取提现记录
     * @return
     */
    @Override
    public ResponseDTO getWithdrawOrdersByTwoAccount(GetWithdrawOrdersByTwoAccountVo getWithdrawOrdersByTwoAccountVo) {
        try {
            // 启动分页拦截器
            PageHelper.startPage(getWithdrawOrdersByTwoAccountVo.getCurrentPage(), getWithdrawOrdersByTwoAccountVo.getPageSize());

            //如果需要根据时间查询
            if(getWithdrawOrdersByTwoAccountVo.getStartDateStr() != null && getWithdrawOrdersByTwoAccountVo.getEndDateStr() != null){
                getWithdrawOrdersByTwoAccountVo.setStartTime(convertStringToTimestamp(getWithdrawOrdersByTwoAccountVo.getStartDateStr()));
                getWithdrawOrdersByTwoAccountVo.setEndTime(convertStringToTimestamp(getWithdrawOrdersByTwoAccountVo.getEndDateStr()));
            }


            // 获取查询结果
            List<NewWithDrawOrder> orders = withdrawOrderMapper.getWithdrawOrdersByTwoAccount(getWithdrawOrdersByTwoAccountVo);

            // 使用查询结果创建 PageInfo
            PageInfo<NewWithDrawOrder> pageInfo = new PageInfo<>(orders);
            // 返回包含分页信息和查询结果的 ResponseDTO
            return new ResponseDTO(200,"查询提现订单成功",pageInfo);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseDTO.default_fail("获取提现订单失败：" + e.getMessage());
        }
    }

    // 将字符串日期转换为 Timestamp
    private static Timestamp convertStringToTimestamp(String dateStr) {
        try {
            // 定义日期格式： yyyy-MM-dd
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // 将 String 转换为 java.util.Date
            java.util.Date parsedDate = dateFormat.parse(dateStr);
            // 将 java.util.Date 转换为 java.sql.Timestamp
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
