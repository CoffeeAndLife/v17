package com.hgz.v17timer.task;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author huangguizhao
 */
public class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+":"+new Date());
        //写你的具体处理逻辑
    }
}
