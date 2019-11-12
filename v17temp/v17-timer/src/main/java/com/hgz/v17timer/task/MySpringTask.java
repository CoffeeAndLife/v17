package com.hgz.v17timer.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author huangguizhao
 */
@Component
public class MySpringTask {

    //@Scheduled(fixedRate = 1000)
    @Scheduled(cron = "* * * * * ?")
    public void run(){
        System.out.println(Thread.currentThread().getName()+"->"+new Date());
    }
}
