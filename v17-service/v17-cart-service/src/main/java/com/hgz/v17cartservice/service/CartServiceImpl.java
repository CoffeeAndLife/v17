package com.hgz.v17cartservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.ICartService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.pojo.CartItem;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.*;
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
        //if(cart != null){
        //TODO
        if(cart.size() != 0){
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
        //1.
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Long delete = redisTemplate.opsForHash().delete(key.toString(), productId);
        if(delete == 1){
            return new ResultBean("200",true);
        }
        return new ResultBean("404",false);
    }

    @Override
    public ResultBean update(String token, Long productId, Integer count) {
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        //获取该记录是否存在
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
        if(cartItem != null){
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
            return new ResultBean("200",true);
        }
        return new ResultBean("404",false);
    }

    @Override
    public ResultBean query(String token) {
        //1.根据token查询到购物车信息
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key.toString());
        //2.
        if(cart.size() > 0){
            //1.获取到values,并没有按照时间来排序
            Collection<Object> values = cart.values();
            //2，存放排序后的结果
            TreeSet<CartItem> treeSet = new TreeSet<>();
            //3, values->treeSet,将无序变为按更新时间排序
            for (Object value : values) {
                CartItem cartItem = (CartItem) value;
                treeSet.add(cartItem);
            }
            //4，treeSet转换为
            List<CartItem> result = new ArrayList<>(treeSet);
            //TODO cartItem->cartItemVO
            return new ResultBean("200",result);
        }
        return new ResultBean("404",null);
    }

    @Override
    public ResultBean merge(String nologinKey, String loginKey) {
        //目标：将未登录购物车合并到已登录购物车
        //1.判断未登录购物车是否存在
        Map<Object, Object> nologinCart = redisTemplate.opsForHash().entries(nologinKey);
        if(nologinCart.size() == 0){
            return new ResultBean("200","无需合并");
        }
        //2.判断已登录购物车是否存在
        Map<Object, Object> loginCart = redisTemplate.opsForHash().entries(loginKey);
        if(loginCart.size() == 0){
            //1,直接将未登录购物车变成已登录购物车即可
            redisTemplate.opsForHash().putAll(loginKey,nologinCart);
            //2.删除未登录的购物车
            redisTemplate.delete(nologinKey);
            return new ResultBean("200","合并成功！");
        }

        //3.两辆购物车都存在，这个时候才需要真正比较
        //3.1 相同的商品，则数量做叠加
        //3.2 否则，直接添加即可
        //以已登录购物车为源头，然后遍历未登录购物车，不断做比较
        Set<Map.Entry<Object, Object>> nologinEntrys = nologinCart.entrySet();
        for (Map.Entry<Object, Object> nologinEntry : nologinEntrys) {
            //nologinEntry.getKey()//productId
            //nologinEntry.getValue()//cartItem
            //判断下，当前已登录购物车是否存在该商品
            CartItem cartItem =
                    (CartItem) redisTemplate.opsForHash().get(loginKey, nologinEntry.getKey());
            if(cartItem != null){
                //存在，则做数量叠加
                CartItem nologinCartItem = (CartItem) nologinEntry.getValue();
                cartItem.setCount(cartItem.getCount()+nologinCartItem.getCount());
                cartItem.setUpdateTime(new Date());
                //TODO
                redisTemplate.opsForHash().put(loginKey,nologinEntry.getKey(),cartItem);
            }else{
                //不存在，直接添加即可
                redisTemplate.opsForHash().put(loginKey,nologinEntry.getKey(),nologinEntry.getValue());
            }
        }
        //删除未登录购物车
        redisTemplate.delete(nologinKey);
        return new ResultBean("200","合并成功！");
    }
}
