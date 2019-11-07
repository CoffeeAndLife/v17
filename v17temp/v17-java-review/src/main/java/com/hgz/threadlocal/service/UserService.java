package com.hgz.threadlocal.service;

import com.hgz.threadlocal.Connection;
import com.hgz.threadlocal.dao.LogDao;
import com.hgz.threadlocal.dao.UserDao;

/**
 * @author huangguizhao
 */
public class UserService {

    //省略接口的声明
    private UserDao userDao = new UserDao();
    private LogDao logDao = new LogDao();

    //事务的边界放在业务层
    //JDBC的封装，connection
    public void add(){
        userDao.add();
        logDao.add();
    }

    public static void main(String[] args){
        UserService userService = new UserService();
        userService.add();
    }
}
