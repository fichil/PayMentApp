package com.cykj.service.impl;

import com.cykj.mapper.PlatformMenuMapper;
import com.cykj.pojo.Menu;
import com.cykj.pojo.NewMenuTree;
import com.cykj.service.MenuService;
import com.cykj.utils.ListInMenuTree;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/9/26 15:44
 * @description:
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    PlatformMenuMapper platformMenuMapper;

    @Override
    public ResponseDTO getMenuList(int roleId) {
        List<Menu> menuList = platformMenuMapper.getMenuByRoleId(roleId);
        ArrayList<NewMenuTree> menus = ListInMenuTree.turnToTree(menuList);
        return ResponseDTO.default_success(menus);
    }
}
