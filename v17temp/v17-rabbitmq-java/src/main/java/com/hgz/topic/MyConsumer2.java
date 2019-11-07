package com.hgz.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author huangguizhao
 * 消费者，去队列获取消息
 */
public class MyConsumer2 {

    private static final String EXCHANGE_NAME = "topic_exchange";
    private static final String QUEUE_NAME = "topic_exchange-queue-2";

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

        //限流，最多只放行一个消息 //叫号系统 1-10
        channel.basicQos(1);

        //申明队列和绑定交换机
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"tiyu.#");

        //3.创建一个消费者对象，并且写好处理消息的逻辑
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("消费者2：接收到的消息是："+message);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //手工确认模式，告知MQ服务器，消息已经被正确处理
                //multiple表示是否批量确认
                //为true，则批量确认，假设envelope.getDeliveryTag()=10，意味着将1-10的消息都确认
                //为false,则只确认10
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        //4.需要让消费者去监听队列
        //autoACK=true:自动确认模式
        //autoACK=false:手工确认模式 (推荐)
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
