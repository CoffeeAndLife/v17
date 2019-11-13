package com.hgz.v17sso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    //明确的结论，是不能
    //思考下为什么不能 4
    @PostMapping("checkLogin4PC")
    public String checkLogin4PC(TUser user, HttpServletRequest request) {
        //1.用户服务做认证，判断当前的用户账号信息是否正确
        ResultBean resultBean = userService.checkLogin(user);
        //2.如果正确，则在服务端保存凭证信息
        if("200".equals(resultBean.getStatusCode())){
            request.getSession().setAttribute("user",user.getUsername());
            //登录成功，默认跳转到首页
            return "redirect:http://localhost:9091";
        }
        //3.否则，跳转回登录页面
        return "index";
    }

    @GetMapping("logout")
    @ResponseBody
    public ResultBean logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return new ResultBean("200",true);
    }

    @GetMapping("logout4PC")
    public String logout4PC(){
        return null;
    }

    @GetMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        Object user = request.getSession().getAttribute("user");
        if(user != null){
            return new ResultBean("200",user);
        }
        return new ResultBean("404",null);
    }

    @GetMapping("checkIsLogin4PC")
    public String checkIsLogin4PC(){
        return null;
    }
}
