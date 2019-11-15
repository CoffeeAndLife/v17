package com.hgz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.TreeSet;

/**
 * @author huangguizhao
 * 保存在redis中的购物车结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable,Comparable<CartItem>{

    private Long productId;

    private Integer count;

    private Date updateTime;

    /**
     * 小于0，放左边
     * 大于0，放右边
     * 定义比较规则
     * @param o
     * @return
     */
    @Override
    public int compareTo(CartItem o) {
        //树展示的顺序是左中右
        //最新操作的放前面
        //return (int) (o.getUpdateTime().getTime() - this.getUpdateTime().getTime());
        //return (int) (this.getUpdateTime().getTime()-o.getUpdateTime().getTime());
        int result = (int) (o.getUpdateTime().getTime() - this.getUpdateTime().getTime());
        if(result == 0){
            return -1;
        }
        return result;
    }

    public static void main(String[] args){
        CartItem cartItem1 = new CartItem(1L,100,new Date());
        CartItem cartItem2 = new CartItem(11L,1100,new Date());
        CartItem cartItem3 = new CartItem(2L,200,new Date(System.currentTimeMillis()+100000));

        TreeSet<CartItem> treeSet = new TreeSet<>();
        treeSet.add(cartItem1);
        treeSet.add(cartItem2);
        treeSet.add(cartItem3);

        for (CartItem cartItem : treeSet) {
            System.out.println(cartItem);
        }
    }
}
