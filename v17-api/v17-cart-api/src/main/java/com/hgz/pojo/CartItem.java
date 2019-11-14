package com.hgz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangguizhao
 * 保存在redis中的购物车结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable{

    private Long productId;

    private Integer count;

    private Date updateTime;
}
