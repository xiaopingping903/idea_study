package com.haier.adp;

import com.haier.adp.common.boot.HaierBanner;
import com.haier.adp.listener.MQConsumeInitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;

/**
 * Created by 01435337 on 2016/11/4.
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan
public class Application implements EmbeddedServletContainerCustomizer{
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class, "classpath:/dubbo-consumer.xml");
        application.setBanner(new HaierBanner());
        application.addListeners(new MQConsumeInitListener());
        application.run(args);
    }
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container){
        container.setPort(8080);
    }
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("50MB");
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    }
}