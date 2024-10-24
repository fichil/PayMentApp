package com.cykj.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/7/30 8:55
 * @description:
 */
@Data
public class MenuTree {
    private String id;
    private int pid;
    private String label;
    private List<MenuTree> children=new ArrayList<>();

    public MenuTree() {
    }

    public MenuTree(Menu menu) {
        this.id=String.valueOf(menu.getMenuId());
        this.label = menu.getMenuName();
    }
}
