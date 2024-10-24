package com.cykj.vo;


import lombok.Data;

/**
 * @Author: fichil
 * @Date: 2024-10-11 14:15
 * @Description: 平台端管理员修改密码
 */
@Data
public class ChangePasswordVo {
    private int id;
    private String oldPassword;
    private String newPassword;
}
