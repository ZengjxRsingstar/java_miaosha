package com.zengjx.miaosha.service;

import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.domain.User;
import com.zengjx.miaosha.result.CodeMsg;
import com.zengjx.miaosha.result.Result;
import com.zengjx.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/15  10:38
 * @Version V1.0
 */
public interface MiaoshaUserService {

    public MiaoshaUser getMiaoshaUserById(Long  id);

   Boolean login(HttpServletResponse response, LoginVo loginVo);
}
