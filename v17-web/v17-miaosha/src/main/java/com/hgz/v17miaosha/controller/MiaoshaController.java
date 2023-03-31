package com.hgz.v17miaosha.controller;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.exception.MiaoshaException;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

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

    //获取真实的秒杀地址需要经过两个步骤
    //1.客户端发起请求，获取到动态生成的path（前提，到了秒杀时间+当前用户已经登录）
    //将path保存到redis中
    //key------------------------------value
    //miaosha:userId:seckillId---------path
    //2.客户端根据动态获取到的path，重新发起第二次请求，去执行执行真正的秒杀
    //判断，path是否合法，查找redis
    //移除掉path，一次性有效

    /**
     *
     * @param userId  当前用户id
     * @param seckillId  当前参与活动id
     * @return
     */
    @GetMapping("getPath")
    @ResponseBody
    public ResultBean getPath(Long userId,Long seckillId){
        try {
            return miaoShaService.getPath(userId,seckillId);
        }catch (MiaoshaException e){
            return new ResultBean("404",e.getMessage());
        }
    }

    @GetMapping("kill/{path}")
    @ResponseBody
    public ResultBean kill(Long userId,Long seckillId,
                           @PathVariable("path") String path){

        try {
            return miaoShaService.kill(userId,seckillId,path);
        }catch (MiaoshaException e){
            //e.getMessage() 失败的具体原因
            return new ResultBean("404",e.getMessage());
        }
    }

    //不直接暴露真实的秒杀接口
    /*@GetMapping("kill")
    @ResponseBody
    public ResultBean kill(Long userId,Long id){
        //TODO 调用用户服务，获取到当前的登录用户
        //假设当前用户为1号用户
        //Long userId = 1L;
        //
        try {
            return miaoShaService.kill(userId,id);
        }catch (MiaoshaException e){
            //e.getMessage() 失败的具体原因
            return new ResultBean("404",e.getMessage());
        }
    }*/
}
