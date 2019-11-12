package com.hgz.api;

import com.hgz.api.pojo.SMSResponse;

/**
 * @author huangguizhao
 */
public interface ISMS {
    //也支持同步调用
    public SMSResponse sendCodeMessage(String phone, String code);

    public SMSResponse sendBirthdayGreetingMessage(String phone,String username);
}
