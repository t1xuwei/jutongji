package com.jutongji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.jutongji.dao")
public class JutongjiApplication {

	public static void main(String[] args) {
		System.out.println("start==========================");
		SpringApplication.run(JutongjiApplication.class, args);
	}
}
