package com.hgz.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangguizhao
 */
public class JedisTest {

    @Test
    public void stringTest(){
        Jedis jedis = new Jedis("192.168.142.137",6379);
        jedis.auth("java1907");
        jedis.set("target","gaodingredis");
        String target = jedis.get("target");
        System.out.println(target);

        for (int i = 0; i < 10; i++) {
            jedis.incr("count");
        }
        String count = jedis.get("count");
        System.out.println(count);
    }

    @Test
    public void otherTest(){
        Jedis jedis = new Jedis("192.168.142.137",6379);
        jedis.auth("java1907");
        jedis.lpush("list","a","b");
        jedis.rpush("list","a","b");
        List<String> list = jedis.lrange("list", 0, -1);

        Long sadd = jedis.sadd("set", "a", "a", "b");
        System.out.println(sadd);

        Map<String, Double> map = new HashMap<>();
        map.put("java",100.0);
        map.put("php",200.0);
        jedis.zadd("hotbook",map);
    }

    @Test
    public void transactionTest(){
        Jedis jedis = new Jedis("192.168.142.137",6379);
        jedis.auth("java1907");
        //批处理
        Transaction transaction = jedis.multi();
        transaction.set("t1","v1");
        transaction.set("t2","v2");
        transaction.exec();
    }
}
