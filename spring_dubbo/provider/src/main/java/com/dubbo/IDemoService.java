package com.dubbo;

/**
 * Created by Administrator on 2016/12/27.
 */
public interface IDemoService {
    /**
     * 对外提供服务
     * @param name
     * @return
     */
    String sayHello(String name);
}
