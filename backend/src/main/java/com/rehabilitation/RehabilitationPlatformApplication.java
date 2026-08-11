package com.rehabilitation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 运动康复医疗管理平台启动类
 * 
 * @author Rehabilitation Platform Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.rehabilitation.mapper")
@EnableAsync
@EnableScheduling
public class RehabilitationPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(RehabilitationPlatformApplication.class, args);
        System.out.println("====================================");
        System.out.println("运动康复医疗管理平台启动成功!");
        System.out.println("接口文档地址: http://localhost:8080/api/doc.html");
        System.out.println("====================================");
    }
}
