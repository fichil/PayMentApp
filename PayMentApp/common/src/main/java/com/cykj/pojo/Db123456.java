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
public class Db123456 {
    /**
    * id
    */
    private Integer id;

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
}