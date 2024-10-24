package com.cykj.service.impl;


import com.cykj.mapper.OrderMerchantMapper;
import com.cykj.mapper.OrderOrderMapper;
import com.cykj.pojo.Db123456;
import com.cykj.pojo.MainDbOrder;
import com.cykj.service.MerchantService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetBillByIdVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-14 15:45
 * @Description: 商户请求服务接口实现类
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private OrderMerchantMapper merchantMapper ;

    //专门用来独立访问各个用户表的mapper
    @Autowired
    private OrderOrderMapper orderMapper;


    /**
     * 根据商户id、时间段获取商户流水并分页
     * @param getBillByIdVo
     * @return
     */
    @Override
    public ResponseDTO getBillById(GetBillByIdVo getBillByIdVo) {
        try {
            // 启动分页拦截器
            PageHelper.startPage(getBillByIdVo.getCurrentPage(), getBillByIdVo.getPageSize());

            if(getBillByIdVo.getStartDateStr() != null && getBillByIdVo.getEndDateStr() != null){
                    getBillByIdVo.setStartDate(convertStringToTimestamp(getBillByIdVo.getStartDateStr()));
                    getBillByIdVo.setEndDate(convertStringToTimestamp(getBillByIdVo.getEndDateStr()));

            }

            // 获取查询结果
            List<Db123456> bills = merchantMapper.getBillById(getBillByIdVo);

            // 使用查询结果创建 PageInfo
            PageInfo<Db123456> pageInfo = new PageInfo<>(bills);
            // 返回包含分页信息和查询结果的 ResponseDTO
            return new ResponseDTO(200,"查询商户管理员成功",pageInfo);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseDTO.default_fail("获取商户管理员失败：" + e.getMessage());
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

    //根据商户编号查询本日数据
    @Override
    public ResponseDTO getInfo(String merchantNumber) {
        //对应数据表
        String DbName = "db"+ merchantNumber;

        //获取日期
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 格式化输出日期
        String formattedToday = today.format(formatter);
        String formattedTomorrow = tomorrow.format(formatter);

        Integer todayCount = orderMapper.getTodayCount(DbName, formattedToday, formattedTomorrow);

        BigDecimal todayPrice = orderMapper.getTodayPrice(DbName, formattedToday, formattedTomorrow);
        if (todayPrice == null){
            todayPrice = new BigDecimal(0);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("todayPrice",todayPrice);
        data.put("todayCount",todayCount);

        return ResponseDTO.default_success(data);
    }

    @Override
    public ResponseDTO addOrder(List<MainDbOrder> orders) {
        for (MainDbOrder order : orders) {
            String dbName = "db"+order.getMerchantNumber();
            orderMapper.addOrder(order,dbName);
        }
        return ResponseDTO.default_success();
    }
}
