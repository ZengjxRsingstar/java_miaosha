package com.zengjx.miaosha.controller;

import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.result.CodeMsg;
import com.zengjx.miaosha.result.Result;
import com.zengjx.miaosha.service.MiaoshaUserService;
import com.zengjx.miaosha.utils.MD5Util;
import com.zengjx.miaosha.vo.LoginVo;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/15  17:58
 * @Version V1.0
 */
@Controller
@RequestMapping("/login")

public class LoginController {

   @Autowired
   private MiaoshaUserService   miaoshaUserService;

   private   static    Logger   logger= LoggerFactory.getLogger(LoginController.class) ;
   @RequestMapping("/do_login1")
   @ResponseBody
   public Boolean  login(HttpServletResponse response,@RequestBody LoginVo   loginVo){
     logger.info("do_login");
       Boolean aBoolean = miaoshaUserService.login(response, loginVo);
       return  aBoolean;
   }

   @RequestMapping("/to_login")
   public    String    toLogin(){

    return "login";

   }


//    @RequestMapping("/do_login")
//    @ResponseBody
//    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
//        logger.info("doLogin "+loginVo.toString());
//        //登录
//        miaoshaUserService.login(loginVo);
//        return Result.success(true);
//    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.getMobile());
        //登录
        logger.info("to_login"+loginVo);
       // Result<CodeMsg> result = miaoshaUserService.login(loginVo);
        Boolean login = miaoshaUserService.login(response, loginVo);
        Result<Boolean> booleanResult= new Result<>();

        booleanResult.setData(login);
        return  booleanResult;
    }


}
