package com.hgz.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IProductTypeService;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProductType;
import com.hgz.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author huangguizhao
 */
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProductTypeService {

    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }
}
