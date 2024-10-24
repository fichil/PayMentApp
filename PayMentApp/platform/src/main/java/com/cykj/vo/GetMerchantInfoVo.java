package com.cykj.vo;


import lombok.Data;

/**
 * @Author: fichil
 * @Date: 2024-10-08 17:07
 * @Description: 根据条件查找商户vo
 */
@Data
public class GetMerchantInfoVo {

    //商户ID
    private String merchantNumber;

    //商户名
    private String merchantName;

    //商户状态
    private int state;

    //当前页
    private int currentPage;

    //每页数据量
    private int pageSize;
}
