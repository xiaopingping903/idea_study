package com.rabbitMq;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

/**
 * 生产者示例：
 */
public class Send {
    //队列名称
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] args) throws java.io.IOException, TimeoutException {
        //创建连接连接到RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ所在主机IP或者主机名
        factory.setHost("localhost");
        //创建一个连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //指定一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //发送的消息
        String message = "hello world!";
        //往队列中发送一条消息
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[x] Sent '" + message + "'");
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
