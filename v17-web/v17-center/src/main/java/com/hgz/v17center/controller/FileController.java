package com.hgz.v17center.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.v17center.pojo.WangEditorResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;


/**
 * @author huangguizhao
 * 处理文件相关的操作
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FastFileStorageClient client;

    @Value("${image.server}")
    private String imageServer;

    @PostMapping("upload")
    public ResultBean<String> upload(MultipartFile file){
        //1.获取到文件的后缀  **.**
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //2.使用client上传图片到fastdfs
        try {
            StorePath storePath = client.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extName, null);
            StringBuilder stringBuilder = new StringBuilder(imageServer).append(storePath.getFullPath());
            return new ResultBean<>("200",stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO 把日志框架整合进来
            return new ResultBean<>("500","当前服务器繁忙，请稍后再试");
        }
    }

    @PostMapping("batchUpload")
    public WangEditorResultBean batchUpload(MultipartFile[] files){
        String[] data = new String[files.length];
        try {
            for (int i=0;i<files.length;i++) {
                String originalFilename = files[i].getOriginalFilename();
                String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                StorePath storePath = client.uploadImageAndCrtThumbImage(files[i].getInputStream(), files[i].getSize(), extName, null);
                StringBuilder stringBuilder = new StringBuilder(imageServer).append(storePath.getFullPath());
                data[i] = stringBuilder.toString();
            }
            return new WangEditorResultBean("0",data);
        }catch (IOException e) {
            e.printStackTrace();
            //TODO 把日志框架整合进来
            return new WangEditorResultBean("1",null);
        }

    }

}
