package com.cykj.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/9/29 10:29
 * @description: 新菜单类型
 */
@Data
public class NewMenuTree {
    private int Id;
    private String path;
    private String component;
    private String name;
    private String meta;
    private String redirect;
    private List<NewMenuTree> children;

    public NewMenuTree() {
    }

    public NewMenuTree(Menu menu){
        path = menu.getMenuPath();
        Id = menu.getMenuId();
        component = menu.getMenuComponent();
        name = menu.getMenuName();
        children = new ArrayList<>();
        meta = menu.getMenuMeta();
        redirect = menu.getMenuRedirect();
    }
}
