package com.zengjx.miaosha.controller;


import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.domain.OrderInfo;
import com.zengjx.miaosha.redis.MiaoshaUserKey;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.result.CodeMsg;
import com.zengjx.miaosha.result.Result;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.service.MiaoshaUserService;
import com.zengjx.miaosha.service.OrderService;
import com.zengjx.miaosha.vo.GoodsVo;
import com.zengjx.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(HttpServletRequest servletRequest , Model model, MiaoshaUser user,
									  @RequestParam("orderId") long orderId) {

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
		MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

		model.addAttribute("user",user);
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return Result.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Result.success(vo);
    }
    
}
