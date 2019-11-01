package com.hgz.v17index.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IProductTypeService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.entity.TProductType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("index")
public class IndexController {

    @Reference
    private IProductTypeService productTypeService;

    @RequestMapping("show")
    public String showIndex(Model model){
        //1.获取到数据
        List<TProductType> list = productTypeService.list();
        //2.传递到前端进行展示
        model.addAttribute("list",list);
        return "index";
    }

    @RequestMapping("listType")
    @ResponseBody
    public ResultBean listType(){
        //1.获取到数据
        List<TProductType> list = productTypeService.list();
        //2.封装返回
        return new ResultBean("200",list);
    }
}
