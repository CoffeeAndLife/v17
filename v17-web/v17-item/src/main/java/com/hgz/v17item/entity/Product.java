package com.hgz.v17item.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author huangguizhao
 */
@Data
@AllArgsConstructor
public class Product {

    private String name;
    private Long price;
    private Date createTime;
}
