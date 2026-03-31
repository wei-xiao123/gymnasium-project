package com.wx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.wx.service.impl")
@MapperScan("com.wx.mapper")
public class CourseServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseServiceApplication.class, args);
    }
}
