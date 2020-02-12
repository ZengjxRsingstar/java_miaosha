package com.zengjx.miaosha.redis;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/2/3  9:02
 * @Version V1.0
 */
public class GoodsKey  extends   BasePrefix {
    public static KeyPrefix gooddetail =new GoodsKey(60,"gd");
    public   static    GoodsKey  goodsList =new GoodsKey(60,"gl");
    public GoodsKey(int  expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }
    //库存
    public static GoodsKey getMiaoshaGoodsStock= new GoodsKey(0, "gs");
}
