package com.hgz.v17springbootredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SpringbootRedisApplicationTests {

	@Resource(name = "redisTemplate1")
	private RedisTemplate<String,Object> redisTemplate;

	@Test
	public void contextLoads() {
		/*Object k1 = redisTemplate.opsForValue().get("k1");
		System.out.println(k1);*/

		Student student = new Student();
		student.setName("zhangsan");
	}

	class Student{
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
