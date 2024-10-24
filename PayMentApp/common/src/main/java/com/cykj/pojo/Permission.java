package com.cykj.pojo;

import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
@Data
public class Permission {
    private Integer permissionId;

    private Integer roleId;

    private Integer menuId;
}