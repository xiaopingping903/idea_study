package com.webservice;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Administrator on 2017/2/9.
 */
@WebService
public class HelloWorldService {
    public String sayHello() {
        return "Hello World";
    }
    public static void main(String[] args) {
        Endpoint.publish("http://127.0.0.1:9999/helloworld",
                new HelloWorldService());
    }
}