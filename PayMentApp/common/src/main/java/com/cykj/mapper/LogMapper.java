package com.cykj.mapper;

import com.cykj.pojo.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
@Mapper
public interface LogMapper {
    int deleteByPrimaryKey(Integer logId);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(Integer logId);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);
}