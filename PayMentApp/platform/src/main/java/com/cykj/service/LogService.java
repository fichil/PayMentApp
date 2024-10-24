package com.cykj.service;


import com.cykj.pojo.Log;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetLogInfoVo;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-09 16:06
 * @Description: 日志管理接口
 */
public interface LogService {

    //根据条件查询日志并分页
    ResponseDTO getLogInfo(GetLogInfoVo getLogInfoVo);
}
