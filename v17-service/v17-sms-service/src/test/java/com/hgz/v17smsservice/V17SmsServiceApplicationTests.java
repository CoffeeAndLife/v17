package com.hgz.v17smsservice;

import com.hgz.api.ISMS;
import com.hgz.api.pojo.SMSResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SmsServiceApplicationTests {

	@Autowired
	private ISMS sms;

	@Test
	public void sendCodeMessageTest() {
		SMSResponse response = sms.sendCodeMessage("13714215816", "123456");
		//ObjectMapper bug
		System.out.println(response);
	}

}
