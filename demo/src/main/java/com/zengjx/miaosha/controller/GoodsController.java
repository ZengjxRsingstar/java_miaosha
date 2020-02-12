package com.zengjx.miaosha.controller;
import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.redis.GoodsKey;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.result.Result;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.service.MiaoshaUserService;
import com.zengjx.miaosha.vo.GoodDetailVo;
import com.zengjx.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private   static Logger logger= LoggerFactory.getLogger(LoginController.class) ;
    @Autowired
    ThymeleafViewResolver   thymeleafViewResolver;
    @Autowired
    ApplicationContext   applicationContext;
	@Autowired
	MiaoshaUserService userService;
	@Autowired
    GoodsService   goodsService ;
	@Autowired
    RedisService redisService;
	
    @RequestMapping(value = "/to_list",produces = "text/html")
   @ResponseBody
    public String list(HttpServletRequest  request,HttpServletResponse  response , Model model, MiaoshaUser user) {
        model.addAttribute("user", user);

        //获取缓存
        String html = redisService.get(GoodsKey.goodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){

            return   html;
        }
        //获取商品列表
        List<GoodsVo> goodsVos = goodsService.listGoods();
        model.addAttribute("goodsList",goodsVos);
        logger.info("goods "+goodsVos);
//        WebContext ctx = new WebContext(request,response,
//                request.getServletContext(),request.getLocale(), model.asMap());
//
//
//        ISpringTemplateEngine templateEngine = thymeleafViewResolver.getTemplateEngine();
//        templateEngine.process("goodslist",ctx);
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.goodsList, "", html);
        }
        return html;
    }
//http://localhost:8080/goods/goods_detail?goodsId=1
    @RequestMapping("/goods_detail")
    public   String toGoodsDetail(HttpServletResponse response ,Model  model,Long  goodsId){

        Map<String, Object> map = model.asMap();
        logger.info("map"+map);
        logger.info("goodsId"+goodsId);

        GoodsVo goodsVoByGoodsId = goodsService.getGoodsVoByGoodsId(goodsId);
        logger.info("good "+goodsVoByGoodsId);
        model.addAttribute("goods",goodsVoByGoodsId);
        return "goods_detail";
    }
    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
   public    String   detail2(HttpServletResponse  response, HttpServletRequest  request, Model  model, MiaoshaUser  user, @PathVariable(value = "goodsId") long  goodsId){
        model.addAttribute("user", user);
      //获取缓存
        String html = redisService.get(GoodsKey.gooddetail, "", String.class);
        if(!StringUtils.isEmpty(html)){

            return   html;
        }
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        logger.info("good"+goodsVo);
        //判断秒杀状态
        long startDate = goodsVo.getStartDate().getTime();
        long   endDate=goodsVo.getEndDate().getTime();

        long now =System.currentTimeMillis();
        int   remainSeconds=0;
        int miaoshaStatus=0;
        if(now<startDate){
            miaoshaStatus=0;
            remainSeconds=(int)(startDate-now)/1000;

        }
        else if( now >endDate){

          remainSeconds=-1;
          miaoshaStatus=2;

        }
        else{
            remainSeconds=0;
            miaoshaStatus=1;
        }

        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        model.addAttribute("goods",goodsVo);
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.goodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public    Result<GoodDetailVo>    detail(HttpServletResponse  response, HttpServletRequest  request, Model  model, MiaoshaUser  user, @PathVariable(value = "goodsId") long  goodsId){
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        logger.info("good"+goodsVo);
        //判断秒杀状态
        long startDate = goodsVo.getStartDate().getTime();
        long   endDate=goodsVo.getEndDate().getTime();

        long now =System.currentTimeMillis();
        int   remainSeconds=0;
        int miaoshaStatus=0;
        if(now<startDate){
            miaoshaStatus=0;
            remainSeconds=(int)(startDate-now)/1000;

        }
        else if( now >endDate){

            remainSeconds=-1;
            miaoshaStatus=2;

        }
        else{
            remainSeconds=0;
            miaoshaStatus=1;
        }
        //调试
        remainSeconds=0;
        miaoshaStatus=1;

//        model.addAttribute("miaoshaStatus",miaoshaStatus);
//        model.addAttribute("remainSeconds",remainSeconds);
//
//        model.addAttribute("goods",goodsVo);
        GoodDetailVo vo = new GoodDetailVo();
        vo.setGoods(goodsVo);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }

   @RequestMapping("/hello")
    public    String   hello(){

        return   "hello";
    }

    
}
