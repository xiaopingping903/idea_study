# dubbo服务调用实例demo介绍


#### 项目结构图
![Mou icon](http://wj.ihaier.com:8080/images/2016/02/02/3353_1454391643972_f3ccdd27d2000e3f9255a7e3e2c48800.jpg)


#### 文件说明

###### 依赖包
服务的调用需要依赖于一些包，maven插件引入<br>
pom.xml如下：

	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
      <modelVersion>4.0.0</modelVersion>

      <groupId>io.terminus</groupId>
      <artifactId>demo</artifactId>
      <version>1.0-SNAPSHOT</version>


      <dependencies>
        <!-- dubbo api for test   （dubbo服务提供方的api用于服务调用）-->
        <dependency>
            <groupId>com.haier.link</groupId>
            <artifactId>link-upper-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- dubbo -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>2.5.3</version>
        </dependency>

        <!-- zookeeper -->
        <dependency>
            <groupId>com.github.sgroschupf</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.1</version>
        </dependency>

      </dependencies>
	</project>


###### 服务消费者
通过spring配置引用远程服务<br>
customer.xml如下：

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
          http://code.alibabatech.com/schema/dubbo
          http://code.alibabatech.com/schema/dubbo/dubbo.xsd
          ">

      <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
      <dubbo:application name="dubbo-test-consumer" />

      <!-- 使用zookeeper广播注册中心暴露发现服务地址 -->
      <dubbo:registry address="zookeeper://10.135.6.210:2181" />
      <!--<dubbo:registry id="qingdaoRegistry" address="10.135.6.210:2181,10.135.6.214:2181,10.135.13.34:2181" protocol="zookeeper"/>-->

      <!-- 生成远程服务代理，可以和本地bean一样使用demoService -->
      <dubbo:reference  id="demoService" interface="com.haier.link.upper.service.UpperPointReadService"
                      version="1.0" check="false" />

	</beans>
	
	
	
###### 服务消费者
通过spring配置，并调用远程服务<br>
test.java如下：

	package io.terminus.demo;

	import com.haier.oms.client.model.InfoSystemCheckGateModel;
	import com.haier.oms.client.model.OutputSystemCheckGateModel;
	import com.haier.oms.client.service.OuterSystemCheckGateService;
	import org.springframework.context.support.ClassPathXmlApplicationContext;

	import java.util.ArrayList;
	import java.util.List;

	/**
 	* Copyright(c) 2016 杭州端点网络科技有限公司
 	* Date: 16/2/2
 	* Time: 上午10:05
 	* Author: luyuzhou .
 	*/
	public class test {

      @SuppressWarnings("resource")
      public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "customer.xml" });
        context.start();
        UpperPointReadService demoService = (UpperPointReadService) context.getBean("demoService");

        // 业务逻辑
        Sign sign = new Sign();
        sign.setKey(1000003L);
        sign.setSecret("***************************");
        
        ResponseOther<Long> input = demoService.upperReadInvalidOfReturn("A0002", "01", sign);
        System.out.println("-----" + input);
        System.out.println("-----" + input.getResult());
        System.out.println("-----" + input.getError());
      }
	}
