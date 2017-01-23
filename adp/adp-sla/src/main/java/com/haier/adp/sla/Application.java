package com.haier.adp.sla;

import com.haier.adp.common.boot.HaierBanner;
import com.haier.adp.sla.util.DynamicDataSourceRegister;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

/**
 * Mail: xiao@terminus.io <br>
 * Date: 2015-11-17 9:15 AM  <br>
 * Author: xiao
 */
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@Import({DynamicDataSourceRegister.class})//注册动态多数据源
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class,
                "classpath:/spring/adp-sla-dubbo-provider.xml");
        application.setBanner(new HaierBanner());
        application.run(args);
    }
}