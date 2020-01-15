package com.zengjx.miaosha.controller;

import com.zengjx.miaosha.result.CodeMsg;
import com.zengjx.miaosha.result.Result;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/13  15:39
 * @Version V1.0
 */
@RestController
public class DemoController {
  @RequestMapping("/hello")
 public  String   hello(){

       return "hello";

 }
 @RequestMapping("/hellosuccess")
 public Result<String>  sucess(){


    return   Result.success("hello success");
 }
 @RequestMapping("/helloerror")
 public  Result<String>   error(){
      return  Result.error(CodeMsg.SERVER_ERROR) ;

 }
 @RequestMapping("/thymeleaf")
 public   String   thymeleaf(Model  model){
      model.addAttribute("name","jjjjj");
      return "thymeleaf";
 }

}
