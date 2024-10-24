package com.cykj.vo;

import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/10/16 10:34
 * @description:
 */
@Data
public class RegisterVo {
    private String account;
    private String password;
    private String code;
    private String twicePassword;
    private String nickname;
}
