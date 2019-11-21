package com.hgz.v17miaosha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author huangguizhao
 */
@Configuration
public class RedisConfig {

    @Bean(name = "myStringRedisTemplate")
    public RedisTemplate<String,Object> getStringRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean(name = "longRedisTemplate")
    public RedisTemplate<String,Long> getLongRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
