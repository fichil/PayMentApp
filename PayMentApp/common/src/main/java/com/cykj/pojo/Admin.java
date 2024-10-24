package com.cykj.pojo;

import java.util.Date;
import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
@Data
public class Admin {
    /**
    * id
    */
    private Integer id;

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