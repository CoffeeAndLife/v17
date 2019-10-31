package com.hgz.v17productservice.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Properties;

/**
 * @author huangguizhao
 */
//@Configuration
public class CommonConfig {

    public static void main(String[] args){

    }

    @Bean
    public PageHelper getPageHelper(){
        PageHelper pageHelper = new PageHelper();
        //设置属性
        Properties properties = new Properties();
        //
        properties.setProperty("dialect","mysql");
        properties.setProperty("reasonable","true");
        //
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
