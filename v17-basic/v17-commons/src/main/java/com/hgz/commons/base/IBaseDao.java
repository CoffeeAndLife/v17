package com.hgz.commons.base;

import java.util.List;

/**
 * @author huangguizhao
 */
public interface IBaseDao<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKeyWithBLOBs(T t);

    int updateByPrimaryKey(T t);

    List<T> list();
}
