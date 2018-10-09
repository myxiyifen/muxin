package com.xiyifen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
// 扫描mybatis mapper路径
@MapperScan(basePackages = "com.xiyifen.mapper")
// 扫描所有需要的包
@ComponentScan(basePackages = {"com.xiyifen","org.n3r.idworker"})
public class MuxinApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuxinApplication.class, args);
	}
}
