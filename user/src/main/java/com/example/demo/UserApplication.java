package com.example.demo;

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

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
