package com.cykj.controller;

import com.cykj.service.MenuService;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李璟瑜
 * @date 2024/9/26 15:39
 * @description:
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    MenuService menuService;

    @RequestMapping("/getMenu")
    @ResponseBody
    public ResponseDTO getMenu(int roleId){
        return menuService.getMenuList(roleId);
    }

}
