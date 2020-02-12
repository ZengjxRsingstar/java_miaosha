package com.zengjx.miaosha.redis;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/2/10  21:13
 * @Version V1.0
 */
public class MiaoshaKey extends   BasePrefix {
    public MiaoshaKey(String prefix) {
        super(prefix);
    }
    public   static   MiaoshaKey   isGoodsOver= new MiaoshaKey("go");
}
