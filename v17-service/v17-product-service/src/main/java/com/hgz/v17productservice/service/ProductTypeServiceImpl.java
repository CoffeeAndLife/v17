package com.hgz.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IProductTypeService;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProductType;
import com.hgz.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huangguizhao
 */
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProductTypeService {

    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }

    /**
     * 重写获取列表的方法，加入缓存的逻辑
     * 硬件
     * 内存读取速度》磁盘读取速度
     * @return
     */
    @Override
    public List<TProductType> list() {
        //1.查询当前缓存是否存在分类信息
        List<TProductType> list = (List<TProductType>) redisTemplate.opsForValue().get("productType:list");
        if (list == null || list.size()==0) {
            //2.缓存不存在，则查询数据库
            list = super.list();
            //3.将查询结果保存到缓存中
            redisTemplate.opsForValue().set("productType:list",list);
        }
        return list;
    }
}
