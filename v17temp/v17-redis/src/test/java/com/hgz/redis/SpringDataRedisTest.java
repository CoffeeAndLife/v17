package com.hgz.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class SpringDataRedisTest {

    //IOC
    //controller service mapper
    //spring整合第三方开发API

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void stringTest(){
        //序列化
        //String String序列化方式
        redisTemplate.opsForValue().set("smallTarget","1个亿");
        Object smallTarget = redisTemplate.opsForValue().get("smallTarget");
        System.out.println(smallTarget);
    }

    @Test
    public void hashTest(){
        redisTemplate.opsForHash().put("book","name","好好学习");
        Object o = redisTemplate.opsForHash().get("book", "name");
        System.out.println(o.toString());

        Object o1 = new Object();
        System.out.println(o1.toString());
    }

    @Test
    public void numberTest(){
        //修改序列化方式
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        //value默认是JDK的序列化方式
        redisTemplate.opsForValue().set("money","1000");
        //不能支持进行数学运算
        redisTemplate.opsForValue().increment("money",1000);
        Object money = redisTemplate.opsForValue().get("money");
        System.out.println(money);
    }

    @Test
    public void otherTest(){
        //方法名没有跟指令的名称一对一
        //方法名本身命名是有意义的
        redisTemplate.opsForValue().increment("money",-1000);
    }

    @Test
    public void runTest(){
        long start = System.currentTimeMillis();
        //
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (int i = 0; i < 100000; i++) {
                    operations.opsForValue().set("k"+i,"v"+i);
                }
                return null;
            }
        });
        //
        long end = System.currentTimeMillis();
        System.out.println(end-start);//19431  1019
    }

    @Test
    public void ttlTest(){
        redisTemplate.opsForValue().set("18918988991","666");
        redisTemplate.expire("18918988991",2, TimeUnit.MINUTES);
        Long expire = redisTemplate.getExpire("18918988991");
        System.out.println(expire);
    }
}
