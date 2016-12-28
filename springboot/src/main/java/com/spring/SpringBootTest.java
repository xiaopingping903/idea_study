package com.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Created by Administrator on 2016/12/22.
 */
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@Import({DynamicDataSourceRegister.class})//注册动态多数据源
public class SpringBootTest {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(SpringBootTest.class,args);
    }
}
