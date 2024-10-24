package com.cykj.pojo;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:48
 * @description:
 */
@Data
public class WithdrawOrder {
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
}