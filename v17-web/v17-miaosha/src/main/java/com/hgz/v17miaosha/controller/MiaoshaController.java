package com.hgz.v17miaosha.controller;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("miaosha")
public class MiaoshaController {

    @Autowired
    private IMiaoShaService miaoShaService;

    @GetMapping("get")
    public String getById(Long id, Model model){
        TMiaoshaProduct product = miaoShaService.getById(id);
        model.addAttribute("product",product);
        return "miaosha_detail.html";
    }

    @GetMapping("kill")
    @ResponseBody
    public ResultBean kill(Long productId){
        //TODO 调用用户服务，获取到当前的登录用户
        //假设当前用户为1号用户
        Long userId = 1L;
        //
        return miaoShaService.kill(userId,productId);
    }
}
