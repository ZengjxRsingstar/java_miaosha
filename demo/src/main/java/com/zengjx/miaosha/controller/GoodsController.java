package com.zengjx.miaosha.controller;


import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.service.MiaoshaUserService;
import com.zengjx.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private   static Logger logger= LoggerFactory.getLogger(LoginController.class) ;
	@Autowired
	MiaoshaUserService userService;
	@Autowired
    GoodsService   goodsService ;
//	@Autowired
//	RedisService redisService;
	
    @RequestMapping("/to_list")
    public String list( Model model, MiaoshaUser user) {
        model.addAttribute("user", user);

        List<GoodsVo> goodsVos = goodsService.listGoods();
        model.addAttribute("goodsList",goodsVos);
        logger.info("goods "+goodsVos);
        return "goods_list";
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


    
}
