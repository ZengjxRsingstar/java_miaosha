package com.zengjx.miaosha.rabbitmq;

import com.zengjx.miaosha.domain.MiaoshaUser;

import java.io.Serializable;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/2/9  19:31
 * @Version V1.0
 */
public class MiaoshaMessge   implements Serializable {

 public MiaoshaUser   user;
 private  long  goodsId;
 private  long    userId;

    public MiaoshaMessge() {
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MiaoshaMessge{" +
                "user=" + user +
                ", goodsId=" + goodsId +
                ", userId=" + userId +
                '}';
    }
}
