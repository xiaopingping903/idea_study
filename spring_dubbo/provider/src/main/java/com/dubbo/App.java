package com.dubbo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext context=new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml","/spring/dubbo_provider.xml"});

        while (true){
            try {
                System.out.println("aaaaaaaaaaaaa");
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
