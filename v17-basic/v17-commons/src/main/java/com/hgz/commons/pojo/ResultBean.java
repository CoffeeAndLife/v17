package com.hgz.commons.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huangguizhao
 * 统一定义了返回json的接口描述类型
 */
@Data
@AllArgsConstructor
public class ResultBean<T> implements Serializable{

    private String statusCode;
    private T data;

}
