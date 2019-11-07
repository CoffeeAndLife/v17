package com.hgz.publish;

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

    private static final String EXCHANGE_NAME = "fanout_exchange";

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

        //3.声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //4.发送消息到交换机上
        for (int i = 0; i < 10; i++) {
            String message = "这是第"+i+"遍说我爱你";
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
        }

        System.out.println("发送消息成功！");
    }
}
