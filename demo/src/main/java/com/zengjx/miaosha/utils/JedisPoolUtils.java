package com.zengjx.miaosha.utils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/23  8:43
 * @Version V1.0
 */
public class JedisPoolUtils {

  private JedisPoolUtils(){}

  public   static   volatile   JedisPool  jedisPool=null;
  public  static   JedisPool   getJedisPoolInstance(){
      if(null==jedisPool){

        synchronized (JedisPoolUtils.class)  {

            if(null==jedisPool){


                JedisPoolConfig poolConfig = new JedisPoolConfig();
                poolConfig.setMaxTotal(1000);//最大连接数
                jedisPool = new JedisPool(poolConfig ,"127.0.0.1",6379);

            }

        }
      }
      return  null;
  }


  public   static   void   release(JedisPool  jedisPool){

      if(jedisPool!=null){

          jedisPool.destroy();
          System.out.println("连接池关闭");
      }

  }

}
