package com.hgz.v17miaosha.task;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huangguizhao
 * 负责定点去扫描秒杀活动表，初始化Redis的信息
 *
 * 1，完成功能
 * 2，优化性能
 * 3，隐藏隐患
 *
 */
@Component
public class MiaoshaTask {

    @Autowired
    private TMiaoshaProductMapper productMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    //声明：正常应该是定点扫描，而不是每隔5秒扫描，这样太频繁了，也没有必要
    @Scheduled(cron = "0/5 * * * * ?")
    public void scan(){
        //startTime<now<endtime and status=0 and check=1 and flag=1
        //1，查询当前可以开启秒杀的活动
        List<TMiaoshaProduct> list = productMapper.getCanStartKillProduct();
        //2. 判断
        if(list != null && !list.isEmpty()){
            for (TMiaoshaProduct product : list) {
                //初始化Redis的信息
                StringBuilder key = new StringBuilder("miaosha:product:").append(product.getId());
                //TODO 目前是10次，能不能变成1次？

                //修改的方案：
                //1.流水线的方式
                //2.一次性保存一个集合
                for (Integer i = 0; i < product.getCount(); i++) {
                    redisTemplate.opsForList().leftPush(key.toString(),product.getProductId());
                }

                //初始化完毕当前的活动信息之后，需要更新当前活动的状态
                //如果不是定点的情况下，此处是有风险的
                product.setMiaoshaStatus("1");
                productMapper.updateByPrimaryKey(product);
            }
            System.out.println("初始完毕Redis集合....");
        }
    }
}
