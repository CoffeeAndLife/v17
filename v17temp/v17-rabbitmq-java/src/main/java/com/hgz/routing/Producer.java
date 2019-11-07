package com.hgz.routing;

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

    private static final String EXCHANGE_NAME = "direct_exchange";

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
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        //4.发送消息到交换机上
        String message = "重大足球消息:国足今晚只要打平就能出线";
        channel.basicPublish(EXCHANGE_NAME,"football",null,message.getBytes());

        String message2 = "重要篮球消息：只要连续赢12场，我们就能出线";
        channel.basicPublish(EXCHANGE_NAME,"basketball",null,message2.getBytes());

        System.out.println("发送消息成功！");
    }
}
