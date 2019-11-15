package com.hgz.v17cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ICartService;
import com.hgz.api.IUserService;
import com.hgz.commons.pojo.ResultBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author huangguizhao
 */
@RestController
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @GetMapping("add/{productId}/{count}")
    public ResultBean add(@PathVariable("productId") Long productId,
                          @PathVariable("count") Integer count,
                          @CookieValue(name = "cart_token",required = false) String cartToken,
                          HttpServletRequest request,
                          HttpServletResponse response){

        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            //说明已登录
            return cartService.add(userToken,productId,count);
        }

        //未登录，创建一个uuid
        if(cartToken == null){
            cartToken = UUID.randomUUID().toString();
        }
        //写cookie到客户端,更新有效期
        reflushCookie(cartToken, response);
        return cartService.add(cartToken,productId,count);
    }

    @GetMapping("delete/{productId}")
    public ResultBean delete(@PathVariable("productId") Long productId,
                             @CookieValue(name = "cart_token",required = false) String cartToken,
                             HttpServletRequest request,
                             HttpServletResponse response){

        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            //说明已登录
            return cartService.del(userToken,productId);
        }

        if(cartToken != null){
            ResultBean resultBean = cartService.del(cartToken, productId);
            reflushCookie(cartToken,response);
            return resultBean;
        }
        return new ResultBean("404",false);
    }

    @GetMapping("update/{productId}/{count}")
    public ResultBean update(@PathVariable("productId") Long productId,
                             @PathVariable("count") Integer count,
                             @CookieValue(name = "cart_token",required = false) String cartToken,
                             HttpServletRequest request,
                             HttpServletResponse response){

        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            //说明已登录
            return cartService.update(userToken,productId,count);
        }

        if(cartToken != null){
            ResultBean resultBean = cartService.update(cartToken, productId, count);
            reflushCookie(cartToken,response);
            return resultBean;
        }
        return new ResultBean("404",false);
    }


    @GetMapping("query")
    public ResultBean query(@CookieValue(name = "cart_token",required = false) String cartToken,
                            HttpServletRequest request,
                            HttpServletResponse response){

        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            //说明已登录
            return cartService.query(userToken);
        }

        if(cartToken != null){
            ResultBean resultBean = cartService.query(cartToken);
            //写cookie到客户端,更新有效期
            reflushCookie(cartToken, response);
            return resultBean;
        }
        return new ResultBean("404",null);
    }


    private void reflushCookie(@CookieValue(name = "cart_token", required = false) String cartToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("cart_token",cartToken);
        cookie.setPath("/");
        cookie.setDomain("qf.com");
        cookie.setMaxAge(15*24*60*60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
