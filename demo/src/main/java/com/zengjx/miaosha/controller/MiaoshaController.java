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

import javax.imageio.ImageIO;
import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
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

  @RequestMapping("/{path}/do_miaosha")
  @ResponseBody
   public   Result<Integer> do_miaosha(HttpServletRequest   servletRequest,Model  model,
                                       @RequestParam (value = "goodsId") Long    goodsId,
                                       @RequestParam(value="path" )String path){
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
      //验证地址是否相同
      boolean checPath = miaoshaService.checPath(user, goodsId, path);
      if(!checPath){
          return  Result.error(CodeMsg.MIAOSHA_PATH_ERROR);
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
    /**
     * 秒杀地址隐藏
     * 先去请求接口获得秒杀地址
     * 1.接口带上PathVarible参数
     * 2.添加生成地址的接口，
     * 3.秒杀收到请求的时候先验证Pathvarible
     *
     *点击秒杀---触发onclick事件---请求服务端地址getMashaPath()
     * -----发送ajax get请求请求，请求参数为goodsId，url=“/miaosha/path”
     * -----controller层的getMiaosha调用miaoshaservic
     * String  path = create.MiashoPath(user,gooodsId,)
     * ---path的组成是：UUID+123456
     * ----path 保存在redis中key为userId+"_"+goodsId
     *
     * 前端接收成功：获得返回地址
     * 调用 doMiaosha(path)
     *
     * doMiaosha(path) 的流程：
     * 发送ajax 请求地址：url="/miaosha/"+path+"do_miaosha"
     * --->sucess: getMiaoshaRessult($("#goodsId").val());
     * --->error:显示错误“客户端”
     *
     * controller:
     * miaosha方法 requestMapping请求地址“/{path}/do_miaosha”
     * path 与redis 中获得path对比
     * 如果比对不成功则返回比对失败
     *
     *
     */
    @ResponseBody
    @RequestMapping(value = "/path")
    public   Result<String>  getMiaoshaPath(HttpServletRequest  request, MiaoshaUser  user,
                                            @RequestParam(value = "goodsId") Long  goodsId,
    @RequestParam(value = "verifyCode",defaultValue = "0") Integer verifyCode
    ){
    // 获得访问次数
        StringBuffer requestURL = request.getRequestURL();
     Integer time = redisService.get(AccessKey.key, ""+requestURL, Integer.class);
      if(time==null){
          redisService.set(AccessKey.key,""+requestURL,1);

      }
      else  if(time<5){
          Long incr = redisService.incr(AccessKey.key, "" + requestURL);
          logger.info(" access  inctr"+incr);
      }else {

          logger.info(" times error "+CodeMsg.MIAOSHA_TIMESERROR);
          return Result.error(CodeMsg.MIAOSHA_TIMESERROR);
      }

        logger.info("test");
     user=getMiaoshauser(request);
        if(user.getId()==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

      //  Integer aveifyCode = Integer.valueOf(verifyCode);
        logger.info("averifyCode "+verifyCode);
        // 验证码是否正确
        boolean ret=    miaoshaService.chechVerifyCode(user, goodsId,verifyCode);
        if(!ret){
            return  Result.error(CodeMsg.MIAOSHA_VERYCODEERROR);
        }



     logger.info("test2"+user);
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
     return Result.success(path);
    }


    public   MiaoshaUser   getMiaoshauser(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
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
        return  miaoshaUseruser;
    }
    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletRequest request,HttpServletResponse  response,
                                              @RequestParam("goodsId")long goodsId) {
        MiaoshaUser   user=  getMiaoshauser(request);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }




}
