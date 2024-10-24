package com.cykj.vo;


import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: fichil
 * @Date: 2024-10-14 15:31
 * @Description: 通过商户id获取商户流水账单
 */
@Data
public class GetBillByIdVo {
    private String merchantNumber;
    private Timestamp startDate;
    private String startDateStr;
    private Timestamp endDate;
    private String endDateStr;
    private int currentPage;
    private int pageSize;

}
