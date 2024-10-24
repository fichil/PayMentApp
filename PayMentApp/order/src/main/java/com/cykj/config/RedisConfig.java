package com.cykj.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author: fichil
 * @Date: 2024-09-29 14:06
 * @Description: Redis配置类
 */
//@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory builder) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(builder);

        //对redisTemplate序列化
        //对key做序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);

        //对value序列化
        FastJsonRedisSerializer<String> objectFastJsonRedisSerializer = new FastJsonRedisSerializer<>(String.class);
        redisTemplate.setValueSerializer(objectFastJsonRedisSerializer);

        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(objectFastJsonRedisSerializer);

        //支持事务
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;

    }
}
