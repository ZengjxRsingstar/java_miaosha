package com.zengjx.miaosha.validator;

import com.zengjx.miaosha.utils.ValidatorUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	private boolean required = false;
	
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {//如果这个值是必须的 判断是否是电话号码
			return ValidatorUtil.isMobile(value);
		}else {//如果这个值不是必须的，
			if(StringUtils.isEmpty(value)) {//如果这个值为空
				return true;
			}else {
				return ValidatorUtil.isMobile(value);//如果这个值不是必须的，不为空
			}
		}
	}

}