package com.haier.adp.config;

import com.haier.adp.common.boot.HaierBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Mail: xiao@terminus.io <br>
 * Date: 2015-11-17 9:15 AM  <br>
 * Author: xiao
 */
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class,
                "classpath:/spring/adp-config-dubbo-provider.xml");
        application.setBanner(new HaierBanner());
        application.run(args);
    }
}