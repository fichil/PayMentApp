package com.cykj.controller;

import com.cykj.service.GridService;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李璟瑜
 * @date 2024/10/11 14:05
 * @description:
 */
@RestController
@CrossOrigin
@RequestMapping("/grid")
public class GridController {

//    Service自动注入
    @Autowired
    GridService gridService;

    @RequestMapping("/getInfo")
    public ResponseDTO getInfo(){
        return gridService.getInfo();
    }

    @RequestMapping("/getTwiceInfo")
    public ResponseDTO getTwiceInfo(){
        return gridService.getTwiceInfo();
    }



}
