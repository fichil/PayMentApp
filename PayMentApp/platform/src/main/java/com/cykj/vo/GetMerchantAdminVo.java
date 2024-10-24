package com.cykj.vo;


import lombok.Data;

/**
 * @Author: fichil
 * @Date: 2024-10-11 11:24
 * @Description: 获取商户管理员VO
 */
@Data
public class GetMerchantAdminVo {
    private int pageSize;
    private int currentPage;
    private String nickname;
    private int state;
    private String storeNumber;
}
