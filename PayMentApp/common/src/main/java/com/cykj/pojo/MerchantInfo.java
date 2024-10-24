package com.cykj.pojo;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/10/11 17:26
 * @description:
 */
@Data
public class MerchantInfo {
    private Integer id;

    private String merchantNumber;

    private String merchantName;

    /**
     * 钱包
     */
    private BigDecimal wallet;

    private Integer state;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 法人电话
     */
    private String tele;

    private Date createTime;

    /**
     * 阿里账户
     */
    private String alipayAccount;

    /**
     * 微信账户
     */
    private String wechatAccount;

    /**
     * 身份证正面
     */
    private String identityFront;

    /**
     * 身份证背面
     */
    private String identityBack;

    /**
     * qr图
     */
    private String qrImg;

    /**
     * 证书
     */
    private String certificate;
}