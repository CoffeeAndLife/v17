package com.hgz.v17item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@Configuration
public class CommonConfig {

    @Bean
    public ThreadPoolExecutor initThreadPoolExecutor(){
        //获取当前服务器的CPU核数
        int processors = Runtime.getRuntime().availableProcessors();
        //自定义的线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                processors,processors*2,1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100));
        return pool;
    }
}
