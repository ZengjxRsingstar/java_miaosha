package com.zengjx.miaosha.service.Impl;
import com.zengjx.miaosha.controller.LoginController;
import com.zengjx.miaosha.domain.*;
import com.zengjx.miaosha.redis.AccessKey;
import com.zengjx.miaosha.redis.MiaoshaKey;
import com.zengjx.miaosha.redis.OrderKey;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.service.MiaoshaService;
import com.zengjx.miaosha.service.OrderService;
import com.zengjx.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/30  22:04
 * @Version V1.0
 */
@Service
public class MiaoshaServiceImpl   implements MiaoshaService {
    private   static Logger logger= LoggerFactory.getLogger(LoginController.class) ;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService  redisService;
//    @Override
//    public boolean miaosha(Long goodsId) {
//
//       //扣减库存
//        GoodsVo  goodsVo =new GoodsVo();
//
//        return goodsService.reduceStock(goodsVo);
//    }
    @Transactional
    @Override
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
     //扣减库存
        boolean bRet = goodsService.reduceStock(goodsVo);
        //生成订单
        OrderInfo   orderInfo =new OrderInfo();
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setStatus(0);
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(goodsVo.getStockCount()-1);
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setUserId(user.getId());
        long orderId = orderService.insertOrder(orderInfo);//返回订单号
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());

        orderService.insertMiaoshaOrder(miaoshaOrder);
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+orderInfo.getUserId()+"_"+orderInfo.getGoodsId(),miaoshaOrder);
        return orderInfo;
    }

    @Override
    public long getMiaoshaResult(Long userId, long goodsId) {


        MiaoshaOrder miaoshaOrder = redisService.get(OrderKey.getMiaoshaOrderByUidGid, "" + userId + "_" + goodsId, MiaoshaOrder.class);
         if(miaoshaOrder!=null){
             return  miaoshaOrder.getOrderId();
         }
         boolean   isOver =getGoodsOver(goodsId);
         if(isOver){
             return  -1;
         }else {
             return  0;
         }


    }

    @Override
    public void setGoodsOver(Long goodsId) {

    }
    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }
    @Override
    public void reset(List<GoodsVo> goodsVoList) {

      goodsService.resetStock(goodsVoList);
      orderService.deleteOrders();

    }

    @Override
    public boolean checPath(MiaoshaUser user, long goodsId, String path) {

        String   oldPath = redisService.get(AccessKey.key, "" + user.getId() + "_" + goodsId,String.class);
        return  path.equals(oldPath);
    }

    @Override
    public String createMiaoshaPath(MiaoshaUser user, long goodsId) {

        String   str= UUID.randomUUID().toString();
        redisService.set(AccessKey.key,""+user.getId()+"_"+goodsId,str);


        return   str;
    }

    @Override
    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {

        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //redisService.set(MiaoshaKey.getMiaoshaVerifyCode,"user",rnd);
        //输出图片
        return image;


    }

    @Override
    public boolean chechVerifyCode(MiaoshaUser user, Long goodsId,Integer   veryCode) {

        Integer vcode = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + ","+goodsId, Integer.class);
        logger.info("verifyCode  redis="+vcode+" ----  "+veryCode);
        return vcode.equals(veryCode);
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    private static char[] ops = new char[] {'+', '-', '*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }


}
