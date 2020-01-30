package com.zengjx.miaosha.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	/**
	 * md  第一次  MD  加密
	 * @param src
	 * @return
	 */
	
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	public static final String salt = "1a2b3c4d";//固定的
	
	public static String inputPassToFormPass(String inputPass) {
		//对  盐进行改造
		String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
		System.out.println(str);
		return md5(str);
	}

	/***
	 * 添加   盐
	 * @param formPass
	 * @param salt
	 * @return
	 */
	
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	public static String inputPassToDbPass(String inputPass, String saltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, saltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
	//	System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
	//System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
		//System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));//b7797cce01b4b131b433b6acf4add449

      test1();
	}


	public   static void    test1(){

		//e10adc3949ba59abbe56e057f20f883e
		//		e10adc3949ba59abbe56e057f20f883e


		System.out.println(DigestUtils.md5Hex("123456"));//e10adc3949ba59abbe56e057f20f883e
		System.out.println(DigestUtils.md5Hex("12123456c3"));
		System.out.println(DigestUtils.md5Hex(inputPassToFormPass("123456")));//加盐


		System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));
	}
}
