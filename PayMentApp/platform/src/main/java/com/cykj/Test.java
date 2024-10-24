package com.cykj;


import com.cykj.utils.SnowflakeIdGenerator;

/**
 * @Author: fichil
 * @Date: 2024-09-29 15:06
 * @Description: 测试类
 */
public class Test {
    public static void main(String[] args) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1,1);
        System.out.println(snowflakeIdGenerator.nextId());

    }
}
