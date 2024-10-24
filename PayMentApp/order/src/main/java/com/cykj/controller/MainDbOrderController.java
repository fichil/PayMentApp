package com.cykj.controller;

import com.cykj.pojo.MainDbOrder;
import com.cykj.service.MainDbOrderService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/10/10 10:52
 * @description:
 */
@RestController
@RequestMapping("/order")
@CrossOrigin
public class MainDbOrderController {

    @Autowired
    MainDbOrderService service;

    @RequestMapping("/getAllOrder")
    public ResponseDTO getAllOrder(@RequestBody OrderVo vo){
        return service.getAllOrder(vo);
    }

    @PostMapping("/saveOrder")
    public ResponseDTO saveOrder(@RequestBody List<MainDbOrder> orders){
        System.out.println("feng来调用");
        ResponseDTO dto = service.saveAlipayOrder(orders);
        return dto;
    }


}
