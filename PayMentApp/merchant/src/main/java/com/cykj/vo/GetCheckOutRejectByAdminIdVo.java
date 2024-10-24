package com.cykj.vo;


import lombok.Data;

/**
 * @Author: fichil
 * @Date: 2024-10-21 16:15
 * @Description: 根据商户管理员id获取商户被拒绝订单并分页
 */
@Data
public class GetCheckOutRejectByAdminIdVo {
    private int currentPage;
    private int pageSize;
    private int adminId;
}
