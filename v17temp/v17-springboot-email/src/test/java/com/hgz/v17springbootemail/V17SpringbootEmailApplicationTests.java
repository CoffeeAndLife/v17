package com.hgz.v17springbootemail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SpringbootEmailApplicationTests {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Test
	public void sendSimpleMailTest() {
		//1.构建邮件对象
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("javaeechina@163.com");
		message.setTo("2678383176@qq.com");
		message.setSubject("热烈祝福我公司与网易达成长期的战略合作关系");
		message.setText("您好，赶紧关注我们吧<a href='https://www.baidu.com'>关注</a>");

		//2.发送邮件
		javaMailSender.send(message);
	}

	@Test
	public void sendHTMLMailTest() throws MessagingException {
		//1.构建邮件对象
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		helper.setFrom("javaeechina@163.com");
		helper.setTo("2678383176@qq.com");
		helper.setSubject("热烈祝福我公司与网易达成长期的战略合作关系");
		helper.setText("您好，赶紧关注我们吧<a href='https://www.baidu.com'>关注</a>",true);

		//2.发送邮件
		javaMailSender.send(message);
	}

	@Test
	public void sendTemplateTest() throws MessagingException {
		//1.构建邮件对象
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		helper.setFrom("javaeechina@163.com");
		helper.setTo("2678383176@qq.com");
		helper.setSubject("【技术兄弟联盟】生日祝福");
		//邮件的内容由模板来产生
		Context context = new Context();
		context.setVariable("username","java1907");
		String content = templateEngine.process("birthday", context);
		//
		helper.setText(content,true);

		//2.发送邮件
		javaMailSender.send(message);
	}

}
