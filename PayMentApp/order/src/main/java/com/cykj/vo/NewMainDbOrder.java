package com.cykj.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 李璟瑜
 * @date 2024/10/12 11:33
 * @description:
 */
@Data
public class NewMainDbOrder {
    /**
     * id
     */
    private Integer id;

    /**
     * 商户ID
     */
    private String merchantNumber;

    /**
     * 生成的订单号（唯一
     */
    private String orderNumber;

    /**
     * 用户的支付账号
     */
    private String customerAccount;

    /**
     * 该次订单的价格
     */
    private BigDecimal price;

    /**
     * 0：人民币 1：美金
     */
    private Integer currency;

    /**
     * 0：支付宝 1：微信
     */
    private Integer channel;

    /**
     * 订单生成时间
     */
    private Date time;
    private String merchantName;
}
