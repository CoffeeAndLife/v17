package com.hgz.v17cart.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ICartService;
import com.hgz.api.IUserService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author huangguizhao
 */
@Component
public class SSOHandler {

    @Reference
    private ICartService cartService;

    @RabbitListener(queues = "sso-queue-cart")
    @RabbitHandler
    public void process(Map<String,String> params){
        String nologinKey = params.get("nologinKey");
        String loginKey = params.get("loginKey");
        //合并购物车
        cartService.merge("user:cart:"+nologinKey,"user:cart:"+loginKey);
    }
}
