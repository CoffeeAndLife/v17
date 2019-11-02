package com.hgz.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hgz.api.IProductService;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProduct;
import com.hgz.entity.TProductDesc;
import com.hgz.mapper.TProductDescMapper;
import com.hgz.mapper.TProductMapper;
import com.hgz.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author huangguizhao
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private TProductDescMapper productDescMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }


    @Override
    @Transactional
    public Long add(ProductVO productVO) {
        //1.添加商品的基本信息
        productMapper.insertSelective(productVO.getProduct());
        //2.添加商品的描述信息
        TProductDesc productDesc = new TProductDesc();
        productDesc.setProductId(productVO.getProduct().getId());
        productDesc.setProductDesc(productVO.getProductDesc());
        productDescMapper.insertSelective(productDesc);
        return productVO.getProduct().getId();
    }
}
