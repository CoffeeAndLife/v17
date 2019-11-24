package com.hgz.v17miaosha.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("code")
public class CodeController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @RequestMapping("/generate")
    @ResponseBody
    public void getCode(HttpServletResponse response){
        //生成验证码 - 保存到服务器
        String text = defaultKaptcha.createText();
        System.out.println(text);

        //根据验证码生成图片
        BufferedImage image = defaultKaptcha.createImage(text);

        //将验证码图片写回客户端
        try {
            ImageIO.write(image, "jpg", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
