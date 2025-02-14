package com.example.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * description: 启动类
 *
 * @author: LJP
 * @date: 2024/11/14 9:32
 */
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.example.*"})
@EnableAsync
@EnableResourceServer
public class UserApplication {
    private final static Logger logger = LoggerFactory.getLogger(UserApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        logger.info("服务启动");
    }
}
