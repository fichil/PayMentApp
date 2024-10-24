package com.cykj.controller;


import com.cykj.service.LogService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetLogInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: fichil
 * @Date: 2024-10-09 15:59
 * @Description: 日志管理控制类
 */
@Controller
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 根据条件查询日志并分页
     * @param getLogInfoVo
     * @return
     */
    @RequestMapping("/getLogInfo")
    @ResponseBody
    public ResponseDTO getLogInfo(@RequestBody GetLogInfoVo getLogInfoVo) {
        System.out.println("time:"+getLogInfoVo.getTime().get(1));
        return logService.getLogInfo(getLogInfoVo);
    }

}
