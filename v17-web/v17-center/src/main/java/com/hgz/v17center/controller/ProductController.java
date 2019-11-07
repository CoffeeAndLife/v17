package com.hgz.v17center.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hgz.api.IProductService;
import com.hgz.commons.constant.MQConstant;
import com.hgz.entity.TProduct;
import com.hgz.vo.ProductVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("product")
public class ProductController {

    //启动时检查机制
    @Reference(check = false)
    private IProductService productService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("get/{id}")
    @ResponseBody
    public TProduct getById(@PathVariable("id") Long id){
        return productService.selectByPrimaryKey(id);
    }

    @RequestMapping("list")
    public String list(Model model){
        //1.获取数据
        List<TProduct> list = productService.list();
        model.addAttribute("list",list);
        //pageInfo
        //2.展示页面
        return "product/list";
    }

    @RequestMapping("page/{pageIndex}/{pageSize}")
    public String page(Model model,
                       @PathVariable("pageIndex") Integer pageIndex,
                       @PathVariable("pageSize") Integer pageSize){
        //1.获取数据
        PageInfo<TProduct> page = productService.page(pageIndex, pageSize);
        model.addAttribute("page",page);
        //2.展示页面
        return "product/list";
    }

    @PostMapping("add")
    public String add(ProductVO vo){
        //处理核心业务逻辑：T1
        Long newId = productService.add(vo);

        //发送一个消息到消息中间件
        //map.put("type","add");
        //map.put("data",newId);
        rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,"product.add",newId);

        //product.a
        //跳转回到第一页 展示的时候，按照添加时间排序
        return "redirect:/product/page/1/1";
    }


}

