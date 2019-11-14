package com.hgz.v17userservice;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.entity.TUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17UserServiceApplicationTests {

	@Autowired
	private IUserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Test
	public void encodeTest(){
		//MD5 不可逆
		//abc ----> def
		//MD5在线解密
		//建立字典表（原文-密文）
		//加大解密的难度，会加盐

		//abc ---->def
		//abc ---->def

		//abc--->def
		//abc--->dei

		String encode1 = passwordEncoder.encode("123456");
		String encode2 = passwordEncoder.encode("123456");
		System.out.println(encode1);
		System.out.println(encode2);
		//$2a$10$Hxs5VhfcNYfN1ReM0E5xzeNs.9ponDNf4QkGPND5zHkwQB3mrJPYK
		//$2a$10$jlsbLJKBwmSb6PjusUcuZ.ljMYhuvfKF2OMqYMGqa3/UeIB4mqbvm
		//$2a$10$ZUsOoJQOVNUUlldS1zSqd.zxdBFdPTwoAXHpHV8cG6CrTL5Y4YI/a
		//$2a$10$ZKmIBkLTr7L8/ONUzCSh2.58BLhH7yInGUG8SQ/s3iEWvPDMqYmZ6
	}

	@Test
	public void decodeTest(){
		System.out.println(passwordEncoder.matches("123456",
				"$2a$10$Hxs5VhfcNYfN1ReM0E5xzeNs.9ponDNf4QkGPND5zHkwQB3mrJPYK"));
		System.out.println(passwordEncoder.matches("123456",
				"$2a$10$ZUsOoJQOVNUUlldS1zSqd.zxdBFdPTwoAXHpHV8cG6CrTL5Y4YI/a"));
	}

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
