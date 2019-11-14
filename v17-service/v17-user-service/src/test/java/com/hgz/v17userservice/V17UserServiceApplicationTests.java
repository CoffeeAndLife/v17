package com.hgz.v17userservice;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.entity.TUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17UserServiceApplicationTests {

	@Autowired
	private IUserService userService;

	@Test
	public void checkLoginTest() {
		//真实测试，保证完整性测试
		//输入正确的信息，要保证正确的结果
		//输入错误的信息，也是有一个正确的反馈
		//bug
		TUser user = new TUser();
		user.setUsername("1234567890");
		user.setPassword("123456");
		ResultBean resultBean = userService.checkLogin(user);
		System.out.println(resultBean.getStatusCode());
	}

}
