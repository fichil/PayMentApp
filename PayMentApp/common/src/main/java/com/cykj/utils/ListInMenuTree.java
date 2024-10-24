package com.cykj.utils;

import com.cykj.pojo.Menu;
import com.cykj.pojo.NewMenuTree;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/9/29 10:50
 * @description:
 */
@Component
public class ListInMenuTree {
//    列表转化
/*    public static ArrayList<NewMenuTree> turnToNewTree(List<Menu> menuList){
        ArrayList<NewMenuTree> menuTrees = new ArrayList<NewMenuTree>();
        for (Menu menu : menuList) {
            NewMenuTree newMenuTree = new NewMenuTree(menu);
            menuTrees.add(newMenuTree);
        }
        return menuTrees;
    }*/

//    遍历
    public static ArrayList<NewMenuTree> turnToTree(List<Menu> menuList){
        ArrayList<NewMenuTree> menuTrees = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getMenuParentId() == 0){
                NewMenuTree newMenuTree = new NewMenuTree(menu);
                menuTrees.add(getChildMenu(newMenuTree,menuList,menu));
            }
        }
        return menuTrees;
    }
//   递归
    public static NewMenuTree getChildMenu(NewMenuTree newMenuTree,List<Menu> menuList, Menu currentMenu){
        for (Menu menu : menuList) {
            if (menu.getMenuParentId() == currentMenu.getMenuId()){
                NewMenuTree newMenuTreeChiled = new NewMenuTree(menu);
                newMenuTree.getChildren().add(newMenuTreeChiled);
//                newMenuTree.getChildren().add(getChildMenu(newMenuTreeChiled,menuList,menu));
            }
        }
        return newMenuTree;
    }

}
