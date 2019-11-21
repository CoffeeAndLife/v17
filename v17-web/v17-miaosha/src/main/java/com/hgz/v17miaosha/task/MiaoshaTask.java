package com.hgz.v17miaosha.task;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
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

    @Resource(name = "longRedisTemplate")
    private RedisTemplate<String,Long> longRedisTemplate;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;


    //声明：正常应该是定点扫描，而不是每隔5秒扫描，这样太频繁了，也没有必要
    @Scheduled(cron = "0/5 * * * * ?")
    public void scanCanStartKillProduct(){
        //startTime<now<endtime and status=0 and check=1 and flag=1
        //1，查询当前可以开启秒杀的活动
        List<TMiaoshaProduct> list = productMapper.getCanStartKillProduct();
        //2. 判断
        if(list != null && !list.isEmpty()){
            for (TMiaoshaProduct product : list) {
                //初始化Redis的信息
                StringBuilder key = new StringBuilder("miaosha:product:").append(product.getId());
                //修改的方案：
                //1.流水线的方式
                /*
                longRedisTemplate.executePipelined(new SessionCallback<Object>() {
                    @Nullable
                    @Override
                    public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                        for (Integer i = 0; i < product.getCount(); i++) {
                            longRedisTemplate.opsForList().leftPush(key.toString(),product.getProductId());
                        }
                        return null;
                    }
                });
                */
                //2.一次性保存一个集合
                Collection<Long> ids = new ArrayList<>(product.getCount());
                for (Integer i = 0; i < product.getCount(); i++) {
                    ids.add(product.getProductId());
                }

                //key------------------------value
                //miaosha:product:1----------List(1001,1001,1001)
                longRedisTemplate.opsForList().leftPushAll(key.toString(),ids);

                //初始化完毕当前的活动信息之后，需要更新当前活动的状态
                //如果不是定点的情况下，此处是有风险的
                product.setMiaoshaStatus("1");
                productMapper.updateByPrimaryKey(product);

                //保存当前活动的信息到Redis中
                //key-------------------------value
                //miaosha:info:1--------------info
                StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(product.getId());
                redisTemplate.opsForValue().set(killInfoKey.toString(),product);
            }
            System.out.println("初始完毕Redis集合....");
        }
    }

    //Redis
    //定期扫描，将当前已经结束的活动，做相关的清理工作
    @Scheduled(cron = "0/5 * * * * ?")
    public void scanCanStopKillProduct(){
        //1，查询当前可以开启秒杀的活动
        List<TMiaoshaProduct> list = productMapper.getCanStopKillProduct();
        //2. 判断
        if(list != null && !list.isEmpty()){
            for (TMiaoshaProduct product : list) {
                //1，清空Redis中相关的信息
                StringBuilder key = new StringBuilder("miaosha:product:").append(product.getId());
                redisTemplate.delete(key.toString());

                StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(product.getId());
                redisTemplate.delete(killInfoKey.toString());

                //2，更新数据库表的记录为已结束
                product.setMiaoshaStatus("2");
                productMapper.updateByPrimaryKey(product);
            }
            //slf4j
            //log
            System.out.println("清理完成....");
        }
    }

}
