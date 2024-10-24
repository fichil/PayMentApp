package com.cykj.service.impl;

import com.cykj.mapper.OrderGridMapper;
import com.cykj.service.GridService;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author 李璟瑜
 * @date 2024/10/11 14:10
 * @description:
 */
@Service
public class GridServiceImpl implements GridService {

    @Autowired
    OrderGridMapper mapper;

    //定义格式
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter monthAndDay = DateTimeFormatter.ofPattern("MM-dd");

//    获得除双端交易信息的数据
    @Override
    public ResponseDTO getInfo() {
        //最总要输出的列表
        HashMap<String,Object> data = new HashMap<>();

        // 获取当前日期
        LocalDate today = LocalDate.now();

        //获得本周初始
        LocalDate threeDayAgo = today.minusDays(3);

        //获得七天交易额
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<BigDecimal> priceList = new ArrayList<>();
        BigDecimal todayPrice = new BigDecimal(0) ;
        int activeCount = 0;
        int todayCount = 0;
        BigDecimal sevenSum = new BigDecimal(0);
        //循环7天
        for (int i = 0; i < 7; i++){
            //获取起始和终止日期
            LocalDate startTime = threeDayAgo.plusDays(i);
            LocalDate endTime = threeDayAgo.plusDays(i+1);
            //格式化
            dateList.add(startTime.format(monthAndDay));
            String startTimeString = startTime.format(formatter);
            String endTimeString = endTime.format(formatter);
            //获得今日总价
            BigDecimal daysSum = mapper.getDaysSum(startTimeString,endTimeString);
            //为空特殊处理
            daysSum = (daysSum == null) ? BigDecimal.ZERO : daysSum;
            sevenSum = sevenSum.add(daysSum);
            priceList.add(daysSum);
            //如果是本日，则额外处理
            if (i == 3){
                todayPrice = daysSum;
                todayCount = mapper.getDaysCount(startTimeString,endTimeString);
                activeCount = mapper.getTodayActiveCount(startTimeString,endTimeString);
            }
        }
        data.put("date",dateList);
        data.put("sevenPrice",priceList);
        data.put("todayPrice",todayPrice);
        data.put("todayCount",todayCount);
        data.put("activeCount",activeCount);
        data.put("sevenDaySum",sevenSum);



        return ResponseDTO.default_success(data);
    }

    @Override
    public ResponseDTO getTwiceInfo() {
        //最总要输出的列表
        HashMap<String,Object> data = new HashMap<>();

        // 获取当前日期
        LocalDate today = LocalDate.now();

        //获得本周初始
        LocalDate threeDayAgo = today.minusDays(3);



        //获取7天交易额
        ArrayList<BigDecimal> wechatList = new ArrayList<>();
        ArrayList<BigDecimal> alipayList = new ArrayList<>();
        //循环
        for (int i = 0; i < 7; i++){
            //获取起始和终止日期
            LocalDate startTime = threeDayAgo.plusDays(i);
            LocalDate endTime = threeDayAgo.plusDays(i+1);
            //格式化
            String startTimeString = startTime.format(formatter);
            String endTimeString = endTime.format(formatter);

            //获得今日总价
            BigDecimal ali = mapper.getDaysSumInAli(startTimeString,endTimeString);
            BigDecimal wechat = mapper.getDaysSumInWechat(startTimeString,endTimeString);

            //为空特殊处理
            ali = (ali == null) ? BigDecimal.ZERO : ali;
            wechat = (wechat == null) ? BigDecimal.ZERO : wechat;

            //放入数组
            alipayList.add(ali);
            wechatList.add(wechat);
        }
        data.put("alipay",alipayList);
        data.put("wechat",wechatList);

        return ResponseDTO.default_success(data);
    }
}
