package com.hgz.v17userservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.hgz.mapper")
public class V17UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(V17UserServiceApplication.class, args);
	}

}
