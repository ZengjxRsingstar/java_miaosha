package com.zengjx.miaosha.controller;

import com.zengjx.miaosha.domain.MiaoshaOrder;
import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.domain.OrderInfo;
import com.zengjx.miaosha.redis.MiaoshaUserKey;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.result.CodeMsg;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.service.MiaoshaService;
import com.zengjx.miaosha.service.OrderService;
import com.zengjx.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/30  22:00
 * @Version V1.0
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    private   static Logger logger= LoggerFactory.getLogger(MiaoshaController.class) ;

    @Autowired
  private GoodsService  goodsService;  
  @Autowired
  private MiaoshaService  miaoshaService;
  @Autowired
  private   OrderService     orderService;
    @Autowired
    private RedisService redisService;

  @RequestMapping("/do_miaosha")
   public   String do_miaosha(HttpServletRequest   servletRequest,Model  model, @RequestParam (value = "goodsId") Long    goodsId
          ){
      Cookie[] cookies = servletRequest.getCookies();
      String token=null;
      for (int i = 0; i <cookies.length ; i++) {

          String name = cookies[i].getName();
          if(name.equals("token")){
              token=cookies[i].getValue();
              break;
          }
      }

      MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

       model.addAttribute("user",user);
       if(user==null){
           return  "login";//退回登录

       }
       //查询是否有库存
      GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
       if(goods.getStockCount()<=0){

        CodeMsg codeMsg=CodeMsg.MIAO_SHA_OVER ;

         model.addAttribute("errmsg",codeMsg.getMsg());
        return "miaosha_fail"  ;
       }
      //查询当前用户是否有订单
      //判断是否已经秒杀到了
      MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
      if(order != null) {
          model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
          return "miaosha_fail";
      }
      OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
      if(orderInfo==null){

          model.addAttribute("errmsg", CodeMsg.MIAO_SHA_ERROR.getMsg());
          return "miaosha_fail";
      }
      model.addAttribute("orderInfo", orderInfo);
      model.addAttribute("goods", goods);
      return  "order_detail";//跳转到秒杀订单。
   }

   @RequestMapping("/test")
   public   String   test(){

     logger.info(" test");
    return "order_detail";
   }
}
