package com.hgz.v17smsservice.handler;

import com.hgz.api.ISMS;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author huangguizhao
 */
@Component
public class SMSHandler {

    @Autowired
    private ISMS sms;

    //处理发送短信验证码
    @RabbitListener(queues = "sms-code-queue")
    @RabbitHandler
    public void processSendCode(Map<String,String> params){
        String identification = params.get("identification");
        String code = params.get("code");
        sms.sendCodeMessage(identification,code);
    }

    //处理发送生日祝福
    @RabbitListener(queues = "sms-birthday-queue")
    @RabbitHandler
    public void processSendBrithdayGreeting(Map<String,String> params){
        String identification = params.get("identification");
        String username = params.get("username");
        sms.sendBirthdayGreetingMessage(identification,username);
    }

}
