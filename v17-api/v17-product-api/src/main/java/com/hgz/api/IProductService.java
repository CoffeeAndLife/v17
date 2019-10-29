package com.hgz.api;

import com.github.pagehelper.PageInfo;
import com.hgz.commons.base.IBaseService;
import com.hgz.entity.TProduct;
import com.hgz.vo.ProductVO;

import java.util.List;

/**
 * @author huangguizhao
 */
public interface IProductService extends IBaseService<TProduct>{

    public PageInfo<TProduct> page(Integer pageIndex,Integer pageSize);

    public Long add(ProductVO productVO);//返回主键
}
