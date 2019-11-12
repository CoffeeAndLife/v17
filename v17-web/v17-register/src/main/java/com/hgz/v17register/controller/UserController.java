package com.hgz.v17register.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.commons.util.CodeUtils;
import com.hgz.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Reference
    private IUserService userService;

    @GetMapping("checkUserNameIsExists/{username}")
    @ResponseBody
    public ResultBean checkUserNameIsExists(@PathVariable("username") String username){
        return userService.checkUserNameIsExists(username);
    }

    @GetMapping("checkPhoneIsExists/{phone}")
    @ResponseBody
    public ResultBean checkPhoneIsExists(@PathVariable("phone") String phone){
        return userService.checkPhoneIsExists(phone);
    }

    @GetMapping("checkEmailIsExists/{email}")
    @ResponseBody
    public ResultBean checkEmailIsExists(@PathVariable("email") String email){
        return userService.checkEmailIsExists(email);
    }

    @PostMapping("generateCode/{identification}")
    @ResponseBody
    public ResultBean generateCode(@PathVariable("identification") String identification){
        return userService.generateCode(identification);
    }

    /**
     * 适合处理异步请求
     * @return
     */
    @PostMapping("register")
    @ResponseBody
    public ResultBean register(TUser user){
        return null;
    }

    /**
     * 适合处理同步请求，跳转到相关页面
     * @return
     */
    @PostMapping("register4PC")
    public String register4PC(TUser user){
        return null;
    }

    @GetMapping("activating")
    public String activating(String token){
        return null;
    }

}
