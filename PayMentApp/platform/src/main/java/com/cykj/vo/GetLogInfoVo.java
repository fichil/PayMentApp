package com.cykj.vo;


import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-09 16:03
 * @Description: 查询日志VO
 */
@Data
public class GetLogInfoVo {

    private Integer adminId;
    private String logs;
    private int currentPage;
    private int pageSize;
    private List<Timestamp> time;

}
