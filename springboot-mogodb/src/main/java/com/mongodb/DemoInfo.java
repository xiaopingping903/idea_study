package com.mongodb;

import org.springframework.data.annotation.Id;

/**
 * 实体类.
 * @author Angel --守护天使
 * @version v.0.1
 * @date 2016年8月18日下午3:15:39
 */
public class DemoInfo {

    //id属性是给mongodb用的，用@Id注解修饰
    @Id
    private String id;

    private String name;

    private int age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "DemoInfo [id=" + id + ", name=" + name + ", age=" + age + "]";
    }
}