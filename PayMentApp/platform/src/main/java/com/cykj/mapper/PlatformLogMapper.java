package com.cykj.mapper;

import com.cykj.pojo.Log;
import com.cykj.vo.GetLogInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
@Mapper
public interface PlatformLogMapper {
    int deleteByPrimaryKey(Integer logId);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(Integer logId);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);

    //获取日志记录
    List<Log> getLogInfo(GetLogInfoVo getLogInfoVo);
}