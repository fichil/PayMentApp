package com.cykj.pojo;

import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/10/8 14:55
 * @description:
 */
@Data
public class Menu {
    /**
     * id
     */
    private Integer menuId;

    /**
     * 菜单名
     */
    private String menuName;

    /**
     * 菜单路由地址
     */
    private String menuPath;

    /**
     * 父级菜单id
     */
    private Integer menuParentId;

    /**
     * 组件
     */
    private String menuComponent;

    /**
     * 参数
     */
    private String menuMeta;

    /**
     * 重定向地址
     */
    private String menuRedirect;
}