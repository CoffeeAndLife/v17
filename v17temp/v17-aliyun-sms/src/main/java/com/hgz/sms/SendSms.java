package com.hgz.sms;

/**
 * @author huangguizhao
 */
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.codec.language.bm.Rule;

/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>

下载jar包，将jar包安装到本地仓库，然后通过坐标的方式来引用这个依赖
*/
public class SendSms {
    public static void main(String[] args) {
        sendCodeMessage("13691981","8888");
    }

    //抽取一个方法出来，方便复用
    public static SMSResponse sendCodeMessage(String phone,String code){
        //accountId authToken
        //凭证信息
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "I4FdCMwZPNg58ErkTHYzP", "OVgNAj8Cmg4Xqt9NXMUXVbwPX2bE");
        IAcsClient client = new DefaultAcsClient(profile);

        //1.0  2.0
        //开源软件
        CommonRequest request = new CommonRequest();
        //request.setMethod(MethodType.POST);
        /*
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        */
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
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return null;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
