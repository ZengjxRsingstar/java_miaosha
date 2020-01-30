package com.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

public class JedisPoolUtil {
	private JedisPoolUtil(){}
	
	public static volatile JedisPool jedisPool = null;
	
	public static JedisPool getJedisPoolInstance(){
		if(null == jedisPool){
			synchronized (JedisPoolUtil.class) {
				if(null == jedisPool){
					JedisPoolConfig poolConfig = new JedisPoolConfig();
					poolConfig.setMaxIdle(1000);
					poolConfig.setMaxIdle(32);
					//poolConfig.setMaxWait(1000*100);
					poolConfig.setTestOnBorrow(true);
					jedisPool = new JedisPool(poolConfig ,"127.0.0.1",6379);
				}
			}
		}
		return jedisPool;
	}
	
	public static void release(JedisPool jedisPool, Jedis jedis){
		if(null != jedis){
			jedisPool.returnResourceObject(jedis);
		}
	}

	public static void main(String[] args) {

		JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
		Jedis jedis = jedisPool.getResource();
		jedis.set("test","testA");

		System.out.println(jedis.get("test"));
       testList(jedisPool);
       release(jedisPool);
	}


	public   static  void testList(JedisPool  jedisPool) {
		try (Jedis jedis = jedisPool.getResource()) {
			// 选择数据库:  SELECT 2
			jedis.select(2);
			// 存储数据到列表中
			// LPUSH
			jedis.lpush("phone_list", "Apple");
			jedis.lpush("phone_list", "Huawei");
			jedis.lpush("phone_list", "XiaoMi");

			// 获取存储的数据并输出: LRANGE phone_list 0 2
			List<String> list = jedis.lrange("phone_list", 0, 2);
			for (int i = 0; i < list.size(); i++) {
				System.out.println("phone_list 列表项为: " + list.get(i));
			}
		}
	}
	public   static   void   release(JedisPool  jedisPool){

		if(jedisPool!=null){

			jedisPool.destroy();
			System.out.println("连接池关闭");
		}

	}

}
