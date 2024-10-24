package com.cykj.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 李璟瑜
 * @date 2024/10/12 11:01
 * @description:
 */
@Data
public class NewWithDrawOrder {
    private Integer id;

    /**
     * 提现金额
     */
    private BigDecimal price;

    /**
     * 手续费
     */
    private String tax;

    /**
     * 提现账户
     */
    private Integer withdrawAccount;

    /**
     * 提现时间
     */
    private Date createTime;

    private String merchantName;
}
