package com.cykj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 李璟瑜
 * @date 2024/10/10 16:22
 * @description:
 */
@Mapper
public interface OrderDbMapper {

    int createDb(@Param("dbName") String dbName);

}
