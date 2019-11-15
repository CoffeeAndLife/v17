package com.hgz.v17cartservice;

import com.hgz.api.ICartService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.pojo.CartItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17CartServiceApplicationTests {

	@Autowired
	private ICartService cartService;

	@Resource(name = "myStringRedisTemplate")
	private RedisTemplate<String,Object> redisTemplate;

	@Test
	public void addTest() {
		cartService.add("123",1L,100);
		cartService.add("123",2L,100);
		ResultBean resultBean = cartService.add("123", 1L, 100);
		System.out.println(resultBean.getData());
	}


	@Test
	public void queryTest(){
		/*ResultBean resultBean = cartService.query("123");
		List<CartItem> cartItems = (List<CartItem>) resultBean.getData();
		for (CartItem cartItem : cartItems) {
			System.out.println(cartItem);
		}*/
		redisTemplate.opsForHash().put("123","1","100");
		Object o = redisTemplate.opsForHash().get("123", "66");
		System.out.println(o);
	}

	@Test
	public void delTest(){
		cartService.del("123",4L);
	}

	@Test
	public void updateTest(){
		//更新一条不存在的记录时，变成了添加了
		cartService.update("123",66L,1000);
	}

}
