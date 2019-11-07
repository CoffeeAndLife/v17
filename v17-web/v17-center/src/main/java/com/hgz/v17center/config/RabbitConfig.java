package com.hgz.v17center.config;

import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangguizhao
 */
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange initProductExchange(){
        TopicExchange productExchange = new TopicExchange(
                MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,true,false);
        return productExchange;
    }
}
