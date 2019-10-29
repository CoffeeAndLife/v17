package com.hgz.vo;

import com.hgz.entity.TProduct;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huangguizhao
 */
@Data
public class ProductVO implements Serializable{
    private TProduct product;
    private String productDesc;
}
