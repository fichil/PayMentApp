package com.cykj.vo;


import lombok.Data;

/**
 * @Author: fichil
 * @Date: 2024-10-12 15:11
 * @Description: 获取未审核列表
 */
@Data
public class GetCheckOutListVo {

    private int pageSize;
    private int currentPage;
    private String leglePerson;
}
