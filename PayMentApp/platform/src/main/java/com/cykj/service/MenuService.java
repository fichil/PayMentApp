package com.cykj.service;

import com.cykj.utils.ResponseDTO;

/**
 * @author 李璟瑜
 * @date 2024/9/26 15:43
 * @description:
 */
public interface MenuService {
    ResponseDTO getMenuList(int roleId);
}
