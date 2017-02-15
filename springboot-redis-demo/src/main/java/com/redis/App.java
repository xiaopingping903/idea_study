package com.redis; /**
 * Created by Administrator on 2017/2/8.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 是Spring Boot项目的核心注解,主要是开启自动配置
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@ComponentScan
@EnableAutoConfiguration
@RestController

public class App {

    @Autowired
    private RedisClient redisClinet;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }


    @RequestMapping("/set")
    public String set(String key, String value) throws Exception{
        redisClinet.set(key, value);
        return "success";
    }

    @RequestMapping("/get")
    public String get(String key) throws Exception {
        return redisClinet.get(key);
    }

}