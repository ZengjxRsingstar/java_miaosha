package com.zengjx.miaosha.utils;

import java.util.UUID;

public class UUIDUtil {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static void main(String[] args) {


		System.out.println(uuid());//abed44b03e914334a55acbdb3369fb6a
	}
}
