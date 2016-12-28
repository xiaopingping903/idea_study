package com.dubbo;

/**
 * Created by Administrator on 2016/12/27.
 */
public class DemoServiceImpl implements IDemoService {
    private int count;
    public String sayHello(String name) {
        System.out.println("method invoke:"+(++count));
        return "Hello:"+name+":"+count;
   }
}
