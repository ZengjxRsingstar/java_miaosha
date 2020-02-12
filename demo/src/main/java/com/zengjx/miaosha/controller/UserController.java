package com.zengjx.miaosha.controller;

import com.zengjx.miaosha.domain.User;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.redis.UserKey;
import com.zengjx.miaosha.result.Result;
import com.zengjx.miaosha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/13  20:31
 * @Version V1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
   @Autowired
   private UserService   userService;

    @RequestMapping("/getUserById/{id}")
    public Result<User>   getUserById( @PathVariable Integer  id)
    {


        Result<User>   result=new Result<User>();
        User user = userService.getUserById(id);
          result.setCode(1);
          result.setData(user);
          result.setMsg("查询成功");
        return  result;
    }


    @RequestMapping(value = "/inseartUser",method = RequestMethod.POST)
    public Result<Integer>   inseartUser(@RequestBody User  user){
      int   ret=   userService.inseartUser(user);
      Result<Integer>   result =new Result<>();
      result.setMsg("插入成功");
      result.setData(ret);
      return   result;
    }

}
