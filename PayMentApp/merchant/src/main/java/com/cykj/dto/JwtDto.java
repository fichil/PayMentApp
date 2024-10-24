package com.cykj.dto;

import java.math.BigDecimal;
import java.util.Date;

public class JwtDto {
    /**
     * id
     */
    private Integer id;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号状态 1启动 0禁用
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 店铺ID
     */
    private String storeNumber;

    private Integer roleId;
}
