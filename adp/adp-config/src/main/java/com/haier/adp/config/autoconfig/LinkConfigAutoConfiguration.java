package com.haier.adp.config.autoconfig;

import com.haier.adp.config.dao.ConfigDao;
import com.haier.adp.config.service.ConfigReadService;
import com.haier.adp.config.service.ConfigReadServiceImpl;
import io.terminus.boot.mybatis.autoconfigure.MybatisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * Desc:
 * Date: 16/2/15 16:40
 * Author: Grancy
 * Mail: guork@terminus.io
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Configuration
@ComponentScan("com.haier.link.sla")
@EnableAutoConfiguration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@Slf4j
public class LinkConfigAutoConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConfigDao configDao;






    @Bean
    public ConfigReadService configReadService() {
        ConfigReadServiceImpl configReadService = new ConfigReadServiceImpl();
        return configReadService;
    }


}
