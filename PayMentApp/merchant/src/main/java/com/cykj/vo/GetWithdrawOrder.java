package com.cykj.vo;


import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: fichil
 * @Date: 2024-10-10 15:13
 * @Description: 获取提现订单Vo
 */
@Data
public class GetWithdrawOrder {
    private String withdrawAccount;
    private int currentPage;
    private int pageSize;
    private String merchantNumber;
    private String startDateStr;
    private String endDateStr;
    private Timestamp startTime;
    private Timestamp endTime;
}
