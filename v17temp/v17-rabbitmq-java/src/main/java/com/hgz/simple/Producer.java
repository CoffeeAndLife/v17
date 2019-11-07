package com.hgz.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 * 生产者，负责发送消息到队列
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接，连接上MQ服务器
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.142.137");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/java1907");
        connectionFactory.setUsername("java1907");
        connectionFactory.setPassword("123");

        Connection connection = connectionFactory.newConnection();

        //2.基于这个连接对象，创建对应的通道
        Channel channel = connection.createChannel();

        //3.声明队列
        //如果该队列不存在，则创建该队列，否则，不创建
        channel.queueDeclare(
                "simple-queue",false,false,false,null);
        //4.发送消息到队列上
        String message = "消息队列是一个非常重要的中间件";
        //我们没有指定交换机
        //实际是使用默认交换机
        channel.basicPublish("","simple-queue",null,message.getBytes());

        System.out.println("发送消息成功！");
    }
}
