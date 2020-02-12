package com.zengjx.miaosha.service.Impl;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.zengjx.miaosha.dao.GoodsDao;
import com.zengjx.miaosha.dao.OrderDao;
import com.zengjx.miaosha.domain.*;
import com.zengjx.miaosha.redis.MiaoshaKey;
import com.zengjx.miaosha.redis.OrderKey;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.service.MiaoshaService;
import com.zengjx.miaosha.service.OrderService;
import com.zengjx.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

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
        return false;
    }

    @Override
    public String createMiaoshaPath(MiaoshaUser user, long gooodsId) {
        return null;
    }

    @Override
    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        return null;
    }
}
