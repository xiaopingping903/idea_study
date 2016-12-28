package com.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args){
        ApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml","/spring/dubbo_consumer.xml"});
        TestService testService= (TestService) context.getBean("aaatest");
        testService.init();
    }
}
