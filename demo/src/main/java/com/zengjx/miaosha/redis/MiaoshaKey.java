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



    public MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public   static   MiaoshaKey   isGoodsOver= new MiaoshaKey("go");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(3000, "vc");
}
