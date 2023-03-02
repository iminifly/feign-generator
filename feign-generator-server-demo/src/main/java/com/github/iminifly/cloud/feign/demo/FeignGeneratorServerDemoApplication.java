package com.github.iminifly.cloud.feign.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FeignGeneratorServerDemoApplication
 *
 * @author xuguofeng
 * @date 2020/12/3 16:42
 */
@SpringBootApplication(scanBasePackages = { "com.github.iminifly.cloud.feign.demo", "com.github.iminifly.cloud.plugin.feign.server" })
@MapperScan({ "com.github.iminifly.cloud.plugin.feign.server.mapper" })
public class FeignGeneratorServerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignGeneratorServerDemoApplication.class, args);
	}
}
