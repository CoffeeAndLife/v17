package com.hgz.v17sso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgz.api.IUserService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.entity.TUser;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("sso")
public class SSOController {

    @Reference
    private IUserService userService;

    @PostMapping("checkLogin")
    @ResponseBody
    public ResultBean checkLogin(TUser user){
        return userService.checkLogin(user);
    }

    //TODO 都很合理，都通了
    //TODO 微服务架构下还适用吗？
    @PostMapping("checkLogin4PC")
    public String checkLogin4PC(TUser user, HttpServletRequest request, HttpServletResponse response) {
        //1.用户服务做认证，判断当前的用户账号信息是否正确
        ResultBean resultBean = userService.checkLogin(user);
        //2.如果正确，则在服务端保存凭证信息
        if("200".equals(resultBean.getStatusCode())){
            //TODO 写cookie给客户端，保存凭证
            //1.获取uuid
            String uuid = (String) resultBean.getData();
            //2.创建cookie对象
            Cookie cookie = new Cookie("user_token",uuid);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            //3.写cookie到客户端
            response.addCookie(cookie);
            //request.getSession().setAttribute("user",user.getUsername());
            //登录成功，默认跳转到首页
            return "redirect:http://localhost:9091";
        }
        //3.否则，跳转回登录页面
        return "index";
    }

    @GetMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name = "user_token",required = false) String token,
                             HttpServletResponse response){
        //request.getSession().removeAttribute("user");
        if(token != null){
            //2.创建cookie对象
            Cookie cookie = new Cookie("user_token",token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            //将cookie清除，设置有效期为0
            cookie.setMaxAge(0);
            //3.写cookie到客户端
            response.addCookie(cookie);
        }
        return new ResultBean("200",true);
    }


    @GetMapping("logout4PC")
    public String logout4PC(){
        return null;
    }

    /*@GetMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        //1.获取cookie，获取user_token的值
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            //遍历查询我们需要的那个cookie
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    //2.去redis中查询，是否存在该凭证信息
                    ResultBean resultBean = userService.checkIsLogin(uuid);
                    return resultBean;
                }
            }
        }
        return new ResultBean("404",null);
    }*/

    @GetMapping("checkIsLogin")
    @CrossOrigin(origins = "*",allowCredentials = "true")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = "user_token",required = false) String uuid){
        //1.获取cookie，获取user_token的值
        if(uuid != null){
            //2.去redis中查询，是否存在该凭证信息
            ResultBean resultBean = userService.checkIsLogin(uuid);
            return resultBean;
        }
        return new ResultBean("404",null);
    }

    @GetMapping("checkIsLogin4JSONP")
    @ResponseBody
    public String checkIsLogin4JSONP(@CookieValue(name = "user_token",required = false) String uuid,
                                     String callback) throws JsonProcessingException {
        //1.获取cookie，获取user_token的值
        if(uuid != null){
            //2.去redis中查询，是否存在该凭证信息
            ResultBean resultBean = userService.checkIsLogin(uuid);
            //
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(resultBean);
            //回调客户端函数 JSONP padding 填充
            return callback+"("+json+")";
        }
        return callback+"()";
    }

    @GetMapping("checkIsLogin4PC")
    public String checkIsLogin4PC(){
        return null;
    }
}
