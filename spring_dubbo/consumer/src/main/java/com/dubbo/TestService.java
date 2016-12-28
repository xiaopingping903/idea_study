package com.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Hello world!
 *
 */
@Service("aaatest")
public class TestService
{
    @Autowired
    IDemoService demoService;
    public void init(){
        for(int i=0;i<10;i++){
            String result=demoService.sayHello("xxxx");
            System.out.println("result:"+result);
        }
    }
}
