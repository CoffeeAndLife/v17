package com.hgz.v17emailservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IEmailService;
import com.hgz.commons.pojo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author huangguizhao
 * TODO 实现一个批量发送邮件/短信，的方式 多线程
 * VIP 100
 */
@Service
public class EmailServiceImpl implements IEmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${email.server}")
    private String emailServer;

    @Override
    public ResultBean sendBirthday(String to, String username) {
        //1.构建邮件对象
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,true);
            helper.setFrom(emailServer);
            helper.setTo(to);
            helper.setSubject("【技术兄弟联盟】生日祝福");
            //邮件的内容由模板来产生
            Context context = new Context();
            context.setVariable("username",username);
            String content = templateEngine.process("birthday", context);
            //
            helper.setText(content,true);

            //2.发送邮件
            javaMailSender.send(message);
            //3.反馈成功
            return new ResultBean("200","ok");
        } catch (MessagingException e) {
            e.printStackTrace();
            //第一次失败，添加一条记录
            //第二次失败，更新该记录的重试次数
        }
        return new ResultBean("500","邮件发送失败");
    }

    @Override
    public ResultBean sendActivate(String to, String username) {
        //马云
        //人成功了之后，说什么都是对的
        //男人的成就跟长相成反比
        return null;
    }
}
