package com.cykj.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 李璟瑜
 * @date 2024/10/15 15:38
 * @description:
 */
@Data
public class MerchantInfoAndAdmin {
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



    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;


    /**
     * 店铺ID
     */
    private String storeNumber;

    private Integer roleId;
}
