package com.hgz.api;

import com.hgz.commons.pojo.ResultBean;

/**
 * @author huangguizhao
 */
public interface ICartService {

    /**
     *
     * @param token 购物车的标识
     * @param productId 购买的商品id
     * @param count 购买的商品数量
     * @return
     */
    public ResultBean add(String token,Long productId,Integer count);

    public ResultBean del(String token,Long productId);

    public ResultBean update(String token,Long productId,Integer count);

    public ResultBean query(String token);

    public ResultBean merge(String nologinKey,String loginKey);
}
