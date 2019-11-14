package com.hgz.v17cartservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.ICartService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.pojo.CartItem;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@Service
public class CartServiceImpl implements ICartService {

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResultBean add(String token, Long productId, Integer count) {
        //1.根据token查询到购物车信息
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key.toString());
        //2.当前购物已存在，且存在该商品，则需要修改商量的数量即可
        if(cart != null){
            //需要当前的购物车是否已经存在该商品
            if (redisTemplate.opsForHash().hasKey(key.toString(),productId)) {
                //如果存在，直接更改数量即可
                CartItem cartItem =
                        (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
                //更改数量
                cartItem.setCount(cartItem.getCount()+count);
                //更新操作时间
                cartItem.setUpdateTime(new Date());
                //保存变更
                redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
                //设置有效期
                redisTemplate.expire(key.toString(),15, TimeUnit.DAYS);

                //返回
                return new ResultBean("200",true);
            }
        }
        //3，其他情况，直接添加商品到购物车即可
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setCount(count);
        cartItem.setUpdateTime(new Date());
        //添加购物项到购物车中
        redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
        //设置有效期
        redisTemplate.expire(key.toString(),15, TimeUnit.DAYS);
        //返回
        return new ResultBean("200",true);
    }

    @Override
    public ResultBean del(String token, Long productId) {
        return null;
    }

    @Override
    public ResultBean update(String token, Long productId, Integer count) {
        return null;
    }

    @Override
    public ResultBean query(String token) {
        return null;
    }
}
