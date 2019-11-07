package com.hgz.threadlocal.dao;

import com.hgz.threadlocal.Connection;
import com.hgz.threadlocal.util.ConnectionUtils;

/**
 * @author huangguizhao
 */
public class UserDao {

    public void add(){
        System.out.println("UserDao add。。。");
        //创建connection对象
        //connection.commit();
        //connection.rollback();
        Connection connection = ConnectionUtils.getConnection();
        System.out.println("UserDao->"+connection);
    }
}
