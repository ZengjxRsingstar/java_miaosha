package com.zengjx.miaosha.redis;

public interface KeyPrefix {
		
	public int expireSeconds();//持续时间
	
	public String getPrefix();
	
}
