package com.hgz.v17miaosha.service.impl;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.exception.MiaoshaException;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@Service
public class MiaoshaServiceImpl implements IMiaoShaService{

    @Autowired
    private TMiaoshaProductMapper productMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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


    public ResultBean killOld(Long userId, Long id) {
        /*
        1，未开始
        2，已结束
        3，抢购空了
        4，抢购过了，不能重复抢
        5，获取抢购权力
        */
        //1.获取到当前秒杀活动的信息
        //TMiaoshaProduct product = productMapper.selectByPrimaryKey(id);
        StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(id);
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(killInfoKey.toString());

        //2.未开始
        if ("0".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动还未开始，请耐心等候");
        }
        //3.已结束
        if("2".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动已结束，请下次再来");
        }

        //4.确权用户信息的key
        StringBuilder key = new StringBuilder("miaosha:user:").append(id);
        //key-----------------value
        //miaosha:user:1------Set(101,102)
        //miaosha:user:2------Set(101,103)
        Boolean isMember = redisTemplate.opsForSet().isMember(key.toString(), userId);
        if (isMember) {
            throw new MiaoshaException("您已经抢购到心仪的商品，请勿重复抢购！");
        }
        //获取当前的活动对应的商品
        StringBuilder killKey = new StringBuilder("miaosha:product:").append(id);
        Long productId = (Long) redisTemplate.opsForList().leftPop(killKey.toString());
        if(productId == null){
            throw new MiaoshaException("当前商品已被抢购一空");
        }
        //获取到了抢购权
        Long result = redisTemplate.opsForSet().add(key.toString(), userId);

        return new ResultBean("200","抢购成功！");
    }


    @Override
    public ResultBean kill(Long userId, Long seckillId, String path) {

        //判断当前的path是否合法
        StringBuilder pathKey = new StringBuilder("miaosha:")
                .append(userId).append(":")
                .append(seckillId);
        Object o = redisTemplate.opsForValue().get(pathKey.toString());
        if(o == null){
            throw new MiaoshaException("不是合法的秒杀路径");
        }
        //一次性有效,用完即删除
        redisTemplate.delete(pathKey.toString());

        /*
        1，未开始
        2，已结束
        3，抢购空了
        4，抢购过了，不能重复抢
        5，获取抢购权力
        */
        //1.获取到当前秒杀活动的信息
        //TMiaoshaProduct product = productMapper.selectByPrimaryKey(id);
        StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(seckillId);
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(killInfoKey.toString());

        //2.未开始
        if ("0".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动还未开始，请耐心等候");
        }
        //3.已结束
        if("2".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动已结束，请下次再来");
        }
        //4.确权用户信息的key
        StringBuilder key = new StringBuilder("miaosha:user:").append(seckillId);
        //获取到了抢购权
        Long result = redisTemplate.opsForSet().add(key.toString(), userId);
        if(result == 0){
            throw new MiaoshaException("您已经抢购到心仪的商品，请勿重复抢购！");
        }
        //key-----------------value
        //miaosha:user:1------Set(101,102)
        //miaosha:user:2------Set(101,103)
        //获取当前的活动对应的商品
        StringBuilder killKey = new StringBuilder("miaosha:product:").append(seckillId);
        Long productId = (Long) redisTemplate.opsForList().leftPop(killKey.toString());
        if(productId == null){
            //移除中奖用户
            redisTemplate.opsForSet().remove(key.toString(),userId);
            throw new MiaoshaException("当前商品已被抢购一空");
        }

        //抢购成功！
        //生成订单
        //user_id,product_id,count(1),product_price,orderNo
        //1.生成订单编号，保证唯一性
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSSS");
        String orderNo = new StringBuilder(dateFormat.format(System.currentTimeMillis()))
                                .append(userId).toString();
        //2.发送消息
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("productId",product.getProductId());
        params.put("count",1);
        params.put("productPrice",product.getSalePrice());
        params.put("orderNo",orderNo);
        rabbitTemplate.convertAndSend("order-exchange","order.create",params);

        return new ResultBean("200",orderNo);
    }

    @Override
    public ResultBean getPath(Long userId, Long seckillId) {
        //1.客户端发起请求，获取到动态生成的path（前提，到了秒杀时间+当前用户已经登录）
        //将path保存到redis中
        //key------------------------------value
        //miaosha:userId:seckillId---------path
        StringBuilder killInfoKey = new StringBuilder("miaosha:info:").append(seckillId);
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(killInfoKey.toString());

        //2.未开始
        if ("0".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动还未开始，请耐心等候");
        }
        //3.已结束
        if("2".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动已结束，请下次再来");
        }

        //可以获取动态的秒杀地址
        String path = UUID.randomUUID().toString();
        //将path保存到redis中
        StringBuilder pathKey = new StringBuilder("miaosha:")
                .append(userId).append(":")
                .append(seckillId);
        redisTemplate.opsForValue().set(pathKey.toString(),path);
        //设置该地址一分钟有效
        redisTemplate.expire(pathKey.toString(),1, TimeUnit.MINUTES);

        return new ResultBean("200",path);
    }
}
