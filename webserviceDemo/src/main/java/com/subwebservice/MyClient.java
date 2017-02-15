package com.subwebservice;

/**
 * Created by Administrator on 2017/2/9.
 */
public class MyClient {
    public static void main(String[] args) {
        HelloWorldServiceService hwss=new HelloWorldServiceService();
        HelloWorldService hws=hwss.getHelloWorldServicePort();
        String msg=hws.sayHello();
        System.out.println(msg);
    }
}
