package com.zengjx.miaosha.redis;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/24  16:03
 * @Version V1.0
 */
public class MiaoshaUserKey  extends   BasePrefix {
    public static  final   int  TOKEN_EXPIRE=3600*24*2;
    public MiaoshaUserKey( int  expireSeconds,  String prefix) {
        super(expireSeconds,prefix);
    }
    public   static   MiaoshaUserKey  token=new MiaoshaUserKey(TOKEN_EXPIRE,"tk");
}
