package com.zengjx.miaosha.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zengjx.miaosha.controller.LoginController;
import com.zengjx.miaosha.result.CodeMsg;
import com.zengjx.miaosha.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
@ControllerAdvice//
@ResponseBody
public class GlobalExceptionHandler {
	private   static Logger logger= LoggerFactory.getLogger(LoginController.class) ;
	@ExceptionHandler(value=Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
		logger.info("GlobalExceptionHandlerlogger","exceptionHandler......");
		e.printStackTrace();
		if(e instanceof GlobalException) {
			GlobalException ex = (GlobalException)e;
			return Result.error(ex.getCm());
		}else if(e instanceof BindException) {//判断是否是绑定异常
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
		}else  if( e  instanceof  RedisException){

			logger.info("redis 保存错误");
			return    Result.error(CodeMsg.REDIS_ERROR.fillArgs(e.getMessage()));
		}

		else {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
	}
}
