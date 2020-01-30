package com.zengjx.miaosha.exception;

import com.zengjx.miaosha.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public  class MyGlobalExceptionHandler{
//指明异常处理的处理类型，可以定义多个方法处理不同异常
private   static Logger logger= LoggerFactory.getLogger(LoginController.class) ;
    @ExceptionHandler(Exception.class)
public ModelAndView customException(Exception e) {
    logger.info("MyGlobalExceptionHandler","customException.......");
    ModelAndView mv = new ModelAndView();
    mv.addObject("message", e.getMessage());
    mv.setViewName("myerror");
    return mv;

}
}