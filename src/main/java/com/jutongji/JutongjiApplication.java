package com.jutongji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.jutongji.repository")
public class JutongjiApplication {

	public static void main(String[] args) {
		System.out.println("start==========================");
		SpringApplication.run(JutongjiApplication.class, args);
	}


}
