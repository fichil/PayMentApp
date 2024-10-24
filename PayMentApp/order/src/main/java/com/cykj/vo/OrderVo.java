package com.cykj.vo;

import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/10/10 11:22
 * @description:
 */
@Data
public class OrderVo {

    private int currentPage;
    private int pageSize;
    private String merchantNumber;
    private String orderNumber;
    private String merchantName;

}
