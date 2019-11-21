package com.hgz.v17miaosha.exception;

/**
 * @author huangguizhao
 * 一般自定义异常，都是继承RuntimeException
 */
public class MiaoshaException extends RuntimeException {

    public MiaoshaException(String message){
        super(message);
    }
}
