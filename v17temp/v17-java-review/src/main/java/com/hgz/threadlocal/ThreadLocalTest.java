package com.hgz.threadlocal;

import java.util.Date;

/**
 * @author huangguizhao
 */
public class ThreadLocalTest {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        //开启多个线程来执行任务
        Task task = new Task();
        new Thread(task).start();
        Thread.sleep(10);
        new Thread(task).start();
    }

    static class Task implements Runnable{
        @Override
        public void run() {
            //t1
            Long result = threadLocal.get();
            //
            if(result == null){
                threadLocal.set(System.currentTimeMillis());
                System.out.println(Thread.currentThread().getName()+"->"+threadLocal.get());
            }
        }
    }

}

