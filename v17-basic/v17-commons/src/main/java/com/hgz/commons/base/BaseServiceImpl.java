package com.hgz.commons.base;

import java.util.List;

/**
 * @author huangguizhao
 * 抽象类：提供基本的实现
 */
public abstract class BaseServiceImpl<T> implements IBaseService<T>{

    public abstract IBaseDao<T> getBaseDao();

    @Override
    public int deleteByPrimaryKey(Long id) {
        return getBaseDao().deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T t) {
        return getBaseDao().insert(t);
    }

    @Override
    public int insertSelective(T t) {
        return getBaseDao().insertSelective(t);
    }

    @Override
    public T selectByPrimaryKey(Long id) {
        return getBaseDao().selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return getBaseDao().updateByPrimaryKeySelective(t);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(T t) {
        return getBaseDao().updateByPrimaryKeyWithBLOBs(t);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return getBaseDao().updateByPrimaryKey(t);
    }

    @Override
    public List<T> list() {
        return getBaseDao().list();
    }
}
