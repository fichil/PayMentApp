package com.cykj.mapper;

import com.cykj.pojo.MainDbOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/10/16 14:34
 * @description:
 */
@Mapper
public interface OrderOrderMapper {
    BigDecimal getTodayPrice(@Param("merchantNumber") String merchantNumber,@Param("startDate") String startDate,@Param("endDate") String endDate);
    Integer getTodayCount(@Param("merchantNumber") String merchantNumber,@Param("startDate") String startDate,@Param("endDate") String endDate);

    int addOrder(@Param("order") MainDbOrder order,@Param("dbName")String dbName);

}
