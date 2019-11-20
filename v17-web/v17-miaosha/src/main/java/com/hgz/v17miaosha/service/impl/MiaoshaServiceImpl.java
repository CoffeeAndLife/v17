package com.hgz.v17miaosha.service.impl;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author huangguizhao
 */
@Service
public class MiaoshaServiceImpl implements IMiaoShaService{

    @Autowired
    private TMiaoshaProductMapper productMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    private static int count = 0;

    @Override
    @Cacheable(value = "product",key = "#id")
    //product::1----------对象
    public TMiaoshaProduct getById(Long id) {
        /*//1.先查询缓存有没有存在该对象
        TMiaoshaProduct product =
                (TMiaoshaProduct) redisTemplate.opsForValue().get("product:" + id);
        //2.判断
        if(product == null){
            product = productMapper.selectByPrimaryKey(id);
            //将查询结果保存到缓存中
            redisTemplate.opsForValue().set("product:"+id,product);
        }
        return product;*/
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public ResultBean kill(Long userId, Long productId) {
        //TODO
        //将数据库的操作逻辑，修改为Redis的操作逻辑

        //1.查询当前被秒杀的商品的库存是否充足
        TMiaoshaProduct product = productMapper.selectByPrimaryKey(productId);
        //2.如果充足，则扣减库存
        if(product.getCount() > 0){//T1 1  T2 1
            product.setCount(product.getCount()-1);
            product.setUpdateTime(new Date());
            productMapper.updateByPrimaryKeySelective(product);
            //productMapper.update(product.getVersion())
            //update t_miaosha_product set count=count-1,version=version+1 where product_id=1 and version=oldVersion
            //TODO 记录谁抢到了 userId---productId
            //TODO 生成订单编号
            System.out.println("抢购到了....."+(++count));
            return new ResultBean("200","抢购成功，正在跳转到订单支付页面");
        }
        //3.记录当前用户获得秒杀商品的权限
        return new ResultBean("404","本次抢购过于凶残，已经被抢购一空");
    }
}
