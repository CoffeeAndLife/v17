package com.hgz.api;

import com.hgz.commons.pojo.ResultBean;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

/**
 * @author huangguizhao
 */
public interface IEmailService {

    /**
     *  发送生日祝福的邮件
     * @param to 收件人地址
     * @param username 用户名
     * @return
     */
    public ResultBean sendBirthday(String to,String username);

    /**
     * 发送激活的邮件
     * @param to 收件人地址
     * @param username 用户名
     * @return
     */
    public ResultBean sendActivate(String to,String username);
}
