package com.zengjx.miaosha.service.Impl;

import com.zengjx.miaosha.dao.MiaoshaUserDao;
import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.exception.GlobalException;
import com.zengjx.miaosha.redis.MiaoshaUserKey;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.result.CodeMsg;
import com.zengjx.miaosha.result.Result;
import com.zengjx.miaosha.service.MiaoshaUserService;
import com.zengjx.miaosha.utils.UUIDUtil;
import com.zengjx.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/15  10:43
 * @Version V1.0
 */
@Service
public class MiaoshaUserServiceImpl  implements MiaoshaUserService {
    public static final String COOKI_NAME_TOKEN = "token";
    private   static Logger logger= LoggerFactory.getLogger(MiaoshaUserServiceImpl.class) ;
    @Autowired
    private MiaoshaUserDao   miaoshaUserDao;
    @Autowired
    private RedisService   redisService;
    @Override
    public MiaoshaUser getMiaoshaUserById(Long id) {
        MiaoshaUser user = miaoshaUserDao.getById(id);
        return user;
    }


    @Override
    public Boolean login(HttpServletResponse response,LoginVo loginVo) {
    Result<CodeMsg>  result  =new Result<>();
    //按照手机号查找用户
        if(loginVo.getMobile()==null){
            CodeMsg  codeMsg  =CodeMsg.MOBILE_NOT_EXIST;
            throw   new GlobalException(codeMsg);
        }
        if(loginVo.getMobile()==null){
            CodeMsg  codeMsg  =CodeMsg.PASSWORD_EMPTY;
            throw   new GlobalException(codeMsg);
        }

        Long aLong = Long.parseLong(loginVo.getMobile());
        logger.info("phone "+aLong);
        MiaoshaUser    miaoshaUser=    miaoshaUserDao.getById(aLong) ;
    // 密码校验

      if(miaoshaUser==null){
       CodeMsg  codeMsg  =CodeMsg.MOBILE_NOT_EXIST;
          throw   new GlobalException(codeMsg);
      }

        String password =loginVo.getPassword();
        if(!password.equals(miaoshaUser.getPassword())){
        CodeMsg  codeMsg =CodeMsg.PASSWORD_ERROR;
          throw   new GlobalException(codeMsg);
        }
      //生成cookie
      String   token= UUIDUtil.uuid();
      addCookie(response,token,miaoshaUser);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser miaoshaUser) {

    //1.user保存在redisService.set
    //2. 保存UUID  token 在Cookie,
    //3.设置cookie的路径为“/”
    //4.response添加cookie
        boolean set = redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
        Cookie  cookie  =new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setPath("/");
        cookie.setMaxAge(MiaoshaUserKey.TOKEN_EXPIRE);
        response.addCookie(cookie);
    }


}
