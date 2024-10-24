package com.cykj.vo;

import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/10/15 10:24
 * @description:登陆用VO
 */
@Data
public class LoginVo {
    private String account;
    private String password;
    private String code;
}
