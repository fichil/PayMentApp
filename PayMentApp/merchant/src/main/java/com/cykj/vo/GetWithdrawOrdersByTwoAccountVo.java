package com.cykj.vo;


import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: fichil
 * @Date: 2024-10-16 16:39
 * @Description: 根据提现账号获取提现记录
 */
@Data
public class GetWithdrawOrdersByTwoAccountVo {

    private String ali;
    private String wx;
    private int currentPage;
    private int pageSize;
    private String startDateStr;
    private String endDateStr;
    private Timestamp startTime;
    private Timestamp endTime;
}
