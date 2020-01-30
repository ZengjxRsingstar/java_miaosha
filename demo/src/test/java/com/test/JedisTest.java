package com.test;

import redis.clients.jedis.Jedis;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/23  8:37
 * @Version V1.0
 */
public class JedisTest {
    public static void main(String[] args) {
        //Jedis  jedis =new Jedis("127.0.0.1",6379);
        Jedis   jedis =new Jedis();
        System.out.println(jedis.ping());
    }
}
