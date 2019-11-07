package com.hgz.v17search.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author huangguizhao
 */
@Component
@RabbitListener(queues = "center-product-exchange-search-queue")
@Slf4j
public class CenterProductHandler {

    //1.声明队列 center-product-exchange-search-queue
    //2.绑定交换机
    //借助管理平台来实现

    @Reference
    private ISearchService searchService;

    @RabbitHandler
    public void process(Long newId){
        log.info("处理了id为：{}的消息",newId);
        searchService.synById(newId);
    }
}
