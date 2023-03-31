package com.hgz.v17miaosha.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 * 目的：演示令牌桶限流的特点
 */
@Controller
@RequestMapping("limit")
public class LimiterController {

    //每秒产生两个令牌
    //private RateLimiter rateLimiter = RateLimiter.create(100);

    @RequestMapping("miaosha")
    @ResponseBody
    public String miaosha(){
        //1.获取令牌
        /*boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            //获取失败，降级处理
            System.out.println("获取令牌失败！正在排队中.....");
            return "获取令牌失败！正在排队中.....";
        }*/
        //2.获取令牌成功，执行秒杀逻辑
        System.out.println("进入秒杀处理逻辑.....");
        return "秒杀处理结果：成功！";
    }

    public static void main(String[] args){
        RateLimiter rateLimiter = RateLimiter.create(10.0);
        System.out.println(rateLimiter.acquire(20));
        System.out.println(rateLimiter.acquire(1));
        System.out.println(rateLimiter.acquire(1));
    }
}
