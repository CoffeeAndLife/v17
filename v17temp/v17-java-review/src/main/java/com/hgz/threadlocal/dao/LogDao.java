package com.hgz.threadlocal.dao;

import com.hgz.threadlocal.Connection;
import com.hgz.threadlocal.util.ConnectionUtils;

/**
 * @author huangguizhao
 */
public class LogDao {

    public void add(){
        System.out.println("LogDao add。。。");
        Connection connection = ConnectionUtils.getConnection();
        System.out.println("LogDao->"+connection);
        //创建connection对象
        //connection.commit();
        //connection.rollback();
    }
}
