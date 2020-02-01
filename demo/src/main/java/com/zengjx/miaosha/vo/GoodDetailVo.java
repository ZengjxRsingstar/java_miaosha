package com.zengjx.miaosha.vo;

import com.zengjx.miaosha.domain.MiaoshaUser;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/29  21:20
 * @Version V1.0
 */
public class GoodDetailVo {
    private   int   miaoshaStatus=0;//秒杀状态
    private  int    remainSeconds=0;//秒杀倒计时
    private  GoodsVo  goods ;//秒杀商品
    private MiaoshaUser  user;

    public GoodDetailVo() {
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "GoodDetailVo{" +
                "miaoshaStatus=" + miaoshaStatus +
                ", remainSeconds=" + remainSeconds +
                ", goods=" + goods +
                ", user=" + user +
                '}';
    }
}
