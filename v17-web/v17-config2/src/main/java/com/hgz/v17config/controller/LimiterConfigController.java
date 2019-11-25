package com.hgz.v17config.controller;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangguizhao
 */
@RestController
@RequestMapping("limiter")
public class LimiterConfigController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //配置MQ回调处理函数
    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.err.println("ack: " + ack);
            if(ack){
                System.out.println("说明消息正确送达MQ服务器");
                //到底是哪个消息被确认了？
                System.out.println("correlationData: " + correlationData.getId());
                //TODO 2更新该消息的状态为已确认，或者直接删除也行
            }
        }
    };

    @GetMapping("change/{newRate}")
    public String change(@PathVariable("newRate") Double newRate){
        //1.修改当前系统的相关的表的数据，将限流的值修改为newRate
        System.out.println("update t_config ....");
        //2.发送消息给到响应的MQ
        //设置消息异步回调处理函数
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //添加一些附加参数,保存为id，作为消息的标识
        CorrelationData correlationData = new CorrelationData(newRate.toString());
        //TODO 1往消息记录表，插入消息
        //发送消息
        rabbitTemplate.convertAndSend("","config-limiter-queue",newRate,correlationData);
        return "success";
    }

    //TODO 3,定时任务，定期扫描消息表，进行补偿重弄发
}
