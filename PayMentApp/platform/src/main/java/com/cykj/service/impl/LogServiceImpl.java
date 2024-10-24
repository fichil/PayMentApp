package com.cykj.service.impl;


import com.cykj.mapper.LogMapper;
import com.cykj.mapper.PlatformLogMapper;
import com.cykj.pojo.Log;
import com.cykj.pojo.MerchantInfo;
import com.cykj.service.LogService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.GetLogInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-09 16:08
 * @Description: 日志管理实现类
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private PlatformLogMapper logMapper;

    /**
     * 根据条件查询日志并分页
     * @param getLogInfoVo
     * @return
     */
    @Override
    public ResponseDTO getLogInfo(GetLogInfoVo getLogInfoVo) {
        try {
            // 启动分页拦截器
            PageHelper.startPage(getLogInfoVo.getCurrentPage(), getLogInfoVo.getPageSize());

            // 获取查询结果
            List<Log> logs = logMapper.getLogInfo(getLogInfoVo);

            // 使用查询结果创建 PageInfo
            PageInfo<Log> pageInfo = new PageInfo<>(logs);
            // 返回包含分页信息和查询结果的 ResponseDTO
            return new ResponseDTO(200,"查询日志信息成功",pageInfo);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseDTO.default_fail("获取日志信息失败：" + e.getMessage());
        }
    }
}
