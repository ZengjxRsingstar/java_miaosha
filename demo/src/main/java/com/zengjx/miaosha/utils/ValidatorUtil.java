package com.zengjx.miaosha.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/***
 * 电话号码有效性11位校验
 */
public class ValidatorUtil {
	
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
	
	public static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}
	
	public static void main(String[] args) {
			System.out.println(isMobile("18046059785"));
			System.out.println(isMobile("1891234123"));
	}
}
