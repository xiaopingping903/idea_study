package com.haier.adp.client;

import com.haier.adp.dto.AlmJiraRelation;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * Created by Administrator on 2017/2/15.
 */
public class Client {
    public static void main(String args[]) throws Exception{
        JaxWsDynamicClientFactory dcf =JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client =dcf.createClient(" http://10.135.12.166:8022/alm-web-new/webservice/AlmJiraRelationWebService?wsdl");
        //getUser 为接口中定义的方法名称  张三为传递的参数   返回一个Object数组
        AlmJiraRelation dto=new AlmJiraRelation();
        dto.setAlmRequestId("1");
        dto.setAlmTaskId("1");
        dto.setJiraEpicId("1");
        dto.setTaskDesc("1");
        dto.setTaskTitle("1");
        Object[] objects=client.invoke("updateRelation",dto);
        //输出调用结果
        System.out.println("*****"+objects[0].toString());
    }
  }
