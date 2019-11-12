package com.hgz.v17timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //开启定时任务的支持
public class V17TimerApplication {

	public static void main(String[] args) {
		SpringApplication.run(V17TimerApplication.class, args);
	}

}
