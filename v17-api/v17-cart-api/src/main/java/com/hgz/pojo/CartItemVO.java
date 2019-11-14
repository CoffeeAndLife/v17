package com.hgz.pojo;

import com.hgz.entity.TProduct;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangguizhao
 * 跟视图层对应的对象 view Object
 */
@Data
@AllArgsConstructor
public class CartItemVO implements Serializable{

    private TProduct product;

    private Integer count;

    private Date updateTime;
}
