package com.zengjx.miaosha.controller;

import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.domain.User;
import com.zengjx.miaosha.result.Result;
import com.zengjx.miaosha.service.MiaoshaUserService;
import com.zengjx.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/15  10:58
 * @Version V1.0
 */
@RestController
@RequestMapping("/miaosha")
public class MiaoshaUserController {
    @Autowired
   private MiaoshaUserService  miaoshaUserService;
    @RequestMapping("/getMiaoshaUserById/{id}")
    public Result<MiaoshaUser> getUserById(@PathVariable Long  id)
    {
        Result<MiaoshaUser>   result=new Result<MiaoshaUser>();
        MiaoshaUser user = miaoshaUserService.getMiaoshaUserById(id);
        result.setCode(1);
        result.setData(user);
        result.setMsg("查询成功");
        return  result;
    }

}
