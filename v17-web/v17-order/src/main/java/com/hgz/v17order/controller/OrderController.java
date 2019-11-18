package com.hgz.v17order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huangguizhao
 * 1.进入订单确认页
 * 1.1 当前身份合法认证，是否已登录（）
 * 1.2 展示相关数据（）
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @RequestMapping("toConfirm")
    public String toConfirm(){

        System.out.println("进入订单确认页逻辑，获取各种数据.....");
        //1.获取到当前用户
        //2.获取当前用户对应的购物车信息
        //3.获取当前用户的收件人信息
        //4.获取付款方式
        //5.获取物流商信息

        //6,跳转到页面展示
        return "confirm";
    }
}
