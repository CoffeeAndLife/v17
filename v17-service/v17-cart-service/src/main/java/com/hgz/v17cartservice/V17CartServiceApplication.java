package com.hgz.v17cartservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.hgz.mapper")
public class V17CartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(V17CartServiceApplication.class, args);
	}

}
