package com.hgz.v17smsservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hgz.api.ISMS;
import com.hgz.api.pojo.SMSResponse;

import java.io.DataInput;
import java.io.IOException;

/**
 * @author huangguizhao
 *
 */
@Service
public class SMSService implements ISMS{

    @Override
    public SMSResponse sendCodeMessage(String phone, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "I4FdCMwZPNg58ErkTHYzP", "OVgNAj8Cmg4Xqt9NXMUXVbwPX2bE");
        IAcsClient client = new DefaultAcsClient(profile);

        //1.固化配置,组合请求参数
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysAction("SendSms");
        request.setSysVersion("2017-05-25");

        request.putQueryParameter("RegionId", "cn-hangzhou");
        //手机号
        request.putQueryParameter("PhoneNumbers", phone);
        //模板信息
        request.putQueryParameter("SignName", "兄弟技术联盟");
        request.putQueryParameter("TemplateCode", "SMS_177246089");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            //2.发送请求。获取结果
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            //3.将返回结果的String转换为一个对象
            /*ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(response.getData(),SMSResponse.class);*/
            Gson gson = new Gson();
            return gson.fromJson(response.getData(),SMSResponse.class);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    //未来这个短信服务，可以支持多种方式的短信
    //生日祝福的短信,申请对应的短信模板
    public SMSResponse sendBirthdayGreetingMessage(String phone,String username){
        //亲爱的${username},今天是您的生日，网站给您送上9折优惠券，赶紧购买吧！
        return  null;
    }
    //积分变更的短信,申请对应的短信模板
}
