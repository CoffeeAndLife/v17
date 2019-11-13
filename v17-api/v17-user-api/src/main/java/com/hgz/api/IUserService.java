package com.hgz.api;

import com.hgz.commons.base.IBaseService;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.entity.TUser;

/**
 * @author huangguizhao
 */
public interface IUserService extends IBaseService<TUser>{

    public ResultBean checkUserNameIsExists(String username);
    public ResultBean checkPhoneIsExists(String phone);
    public ResultBean checkEmailIsExists(String email);

    public ResultBean generateCode(String identification);

    ResultBean checkLogin(TUser user);

    //目前分析得到的结论
    //添加用户，是否可以用默认的实现？
    //可以。

    //激活用户，改变用户的状态值，也不需要写
}
