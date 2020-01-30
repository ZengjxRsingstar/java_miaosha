package com.zengjx.miaosha.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//可以用于方法   域    构造      参数
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)//运行时@Retention 注解  修饰注解的生存周期
@Documented//@Documented 注解功能：指明修饰的注解，可以被例如javadoc此类的工具文档化，只负责标记，没有成员取值。
@Constraint(validatedBy = {IsMobileValidator.class })
//自定义注解
public @interface  IsMobile {
	
	boolean required() default true;
	
	String message() default "手机号码格式错误！！！！";//自定义 异常信息

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
