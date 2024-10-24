package com.cykj.vo;


import lombok.Data;

/**
 * @Author: fichil
 * @Date: 2024-09-29 17:00
 * @Description: 管理员登陆vo
 */
@Data
public class LoginVo {
    private String username;
    private String password;
    private String code;
}
