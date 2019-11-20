package com.hgz.v17miaosha.mapper;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;

import java.util.List;

public interface TMiaoshaProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TMiaoshaProduct record);

    int insertSelective(TMiaoshaProduct record);

    TMiaoshaProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TMiaoshaProduct record);

    int updateByPrimaryKey(TMiaoshaProduct record);

    List<TMiaoshaProduct> getCanStartKillProduct();
}