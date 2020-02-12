package com.zengjx.miaosha.controller;

import com.zengjx.miaosha.domain.MiaoshaOrder;
import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.domain.OrderInfo;
import com.zengjx.miaosha.rabbitmq.MQSender;
import com.zengjx.miaosha.rabbitmq.MiaoshaMessge;
import com.zengjx.miaosha.redis.*;
import com.zengjx.miaosha.result.CodeMsg;
import com.zengjx.miaosha.result.Result;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.service.MiaoshaService;
import com.zengjx.miaosha.service.OrderService;
import com.zengjx.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.zengjx.miaosha.redis.GoodsKey.goodsList;
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
public class MiaoshaController  implements InitializingBean {
    private   static Logger logger= LoggerFactory.getLogger(MiaoshaController.class) ;

  @Autowired
  private GoodsService  goodsService;  
  @Autowired
  private MiaoshaService  miaoshaService;
  @Autowired
  private   OrderService     orderService;
  @Autowired
  private RedisService redisService;

  @Autowired
  private MQSender  mqSender;
  private HashMap<Long,Boolean>   localOverMap =   new  HashMap<Long,Boolean>();

  @RequestMapping("/do_miaosha")
  @ResponseBody
   public   Result<Integer> do_miaosha(HttpServletRequest   servletRequest,Model  model, @RequestParam (value = "goodsId") Long    goodsId
          ){
      Cookie[] cookies = servletRequest.getCookies();
      if(cookies==null||cookies.length==0){
          return  null;
      }
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
           return  Result.error(CodeMsg.SESSION_ERROR);
       }
       //内存标记减少redis 访问
       //查询是否有库存
      GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
       if(goods.getStockCount()<=0){
        return Result.error(CodeMsg.MIAO_SHA_OVER) ;
       }
      //查询当前用户是否有订单
      //判断是否已经秒杀到了
      MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
      if(order != null) {
          model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
          return Result.error(CodeMsg.REPEATE_MIAOSHA);
      }
      //放入队列
      MiaoshaMessge    miaoshaMessge =new MiaoshaMessge();
      miaoshaMessge.setGoodsId(goodsId);
      miaoshaMessge.setUser(user);
      miaoshaMessge.setUserId(user.getId());
      mqSender.sendMiaoshaMessage(miaoshaMessge);
     return Result.success(0);//返回0 表示排队中
//      OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//      if(orderInfo==null){
//
//          model.addAttribute("errmsg", CodeMsg.MIAO_SHA_ERROR.getMsg());
//          return "miaosha_fail";
//      }
//      model.addAttribute("orderInfo", orderInfo);
//      model.addAttribute("goods", goods);
//      return  "order_detail";//跳转到秒杀订单。
   }
   @RequestMapping("/test")
   public   String   test(){

     logger.info(" test");
    return "order_detail";
   }
   // 系统初始化---保存每个商品的库存
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo>  goodsVoList =goodsService.listGoods();
        if(goodsVoList==null){
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
          localOverMap.put(goodsVo.getId(),false);
        }
        //logger.info("after......");
    }
    /***
     * 复位
     * @param model
     * @return
     */
  @RequestMapping(value = "/reset",method = RequestMethod.GET)
  @ResponseBody
  public  Result<Boolean>     reset(Model model){
  List<GoodsVo>  goodsVoList =goodsService.listGoods();
      for (GoodsVo goodsVo : goodsVoList) {
          goodsVo.setStockCount(10);
          redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goodsVo.getId(),10);
         localOverMap.put(goodsVo.getId(),false);
      }
      redisService.delete(OrderKey.getMiaoshaOrderByUidGid);//删除秒杀订单
      redisService.delete(MiaoshaKey.isGoodsOver);//删除秒杀
      miaoshaService.reset(goodsVoList);
      return Result.success(true);
  }
  @RequestMapping(value = "/result",method =RequestMethod.GET)
  @ResponseBody
    public   Result<Long>    miashaResult(HttpServletRequest   servletRequest,Model  model,MiaoshaUser   user,@RequestParam("goodsId")long  goodsId)

  {
      Cookie[] cookies = servletRequest.getCookies();
      if(cookies==null||cookies.length==0){
          return  null;
      }
      String token=null;
      for (int i = 0; i <cookies.length ; i++) {

          String name = cookies[i].getName();
          if(name.equals("token")){
              token=cookies[i].getValue();
              break;
          }
      }

      MiaoshaUser miaoshaUseruser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

      Map<String, Object> asMap = model.asMap();
      logger.info("asMap:"+asMap);
      MiaoshaUser   user1 = (MiaoshaUser) asMap.get("user");
      logger.info("user "+user1);
      if(user==null){
        return  Result.error(CodeMsg.SESSION_ERROR) ;
     }
     long    result=miaoshaService.getMiaoshaResult(miaoshaUseruser.getId(),goodsId);

      return  Result.success(result);
  }
}
