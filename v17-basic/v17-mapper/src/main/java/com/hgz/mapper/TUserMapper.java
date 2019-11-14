package com.hgz.mapper;

import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TUser;

public interface TUserMapper extends IBaseDao<TUser>{

    TUser selectByIdentification(String username);
}