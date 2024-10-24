package com.cykj.controller;

import com.cykj.service.DbService;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李璟瑜
 * @date 2024/10/10 16:17
 * @description:
 */
@RestController
@CrossOrigin
@RequestMapping("/db")
public class DbController {

    @Autowired
    DbService service;

    @RequestMapping("/createDb")
    public ResponseDTO createDb(String merchantNumber){
        return service.createDb(merchantNumber);
    }

}
