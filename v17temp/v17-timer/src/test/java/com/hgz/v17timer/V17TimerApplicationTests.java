package com.hgz.v17timer;

import com.hgz.v17timer.task.MyTimerTask;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Timer;

@SpringBootTest
class V17TimerApplicationTests {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args){
		MyTimerTask task = new MyTimerTask();
		Timer timer = new Timer();

		//按照固定的时间间隔，反复执行 5分钟,单线程
		timer.schedule(task,1000,1000);
		//执行一次定时任务
		//timer.schedule(task,1000);

		//每月的15号8:00，给每个好朋友发一个888的小红包
		//1月15 2月15 3月15
		//非固定时间间隔的方式，不建议采用此种方式
		//正则表达式，能看懂，能修改即可
		//cron表达式，能看懂，能修改即可
	}

}
