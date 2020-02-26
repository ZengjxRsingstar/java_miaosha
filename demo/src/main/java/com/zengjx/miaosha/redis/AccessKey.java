package com.zengjx.miaosha.redis;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/2/16  17:45
 * @Version V1.0
 */
public class AccessKey  extends  BasePrefix {
    public   static    AccessKey  key =new AccessKey(120,"access");
    public AccessKey(String prefix) {
        super(prefix);
    }

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
