package com.cykj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author 李璟瑜
 * @date 2024/10/11 14:09
 * @description:
 */
@Mapper
public interface OrderGridMapper {
    BigDecimal getDaysSum(@Param("startTime") String startTime,@Param("endTime") String endTime);
    int getDaysCount(@Param("startTime") String startTime,@Param("endTime") String endTime);
    int getTodayActiveCount(@Param("startTime") String startTime,@Param("endTime") String endTime);
    BigDecimal getDaysSumInAli(@Param("startTime") String startTime,@Param("endTime") String endTime);
    BigDecimal getDaysSumInWechat(@Param("startTime") String startTime,@Param("endTime") String endTime);

}
