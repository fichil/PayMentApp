package com.cykj.pojo;

import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/10/16 9:41
 * @description:
 */
@Data
public class CheckOut {
    private Integer id;

    /**
     * 和admin绑定
     */
    private Integer adminId;

    private String leglePerson;

    private String tele;

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
     * 1:通过；0未通过
     */
    private Integer state;

    /**
     * 原因
     */
    private String reason;
}