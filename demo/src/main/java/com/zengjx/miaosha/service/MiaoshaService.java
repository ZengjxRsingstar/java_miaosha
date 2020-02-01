package com.zengjx.miaosha.service;

import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.domain.OrderInfo;
import com.zengjx.miaosha.vo.GoodsVo;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/30  22:04
 * @Version V1.0
 */
public interface MiaoshaService {
   //public boolean miaosha(Long goodsId);

   public OrderInfo    miaosha(MiaoshaUser   user, GoodsVo  goodsVo);
   public   long  getMiaoshaResult(Long  userId,long goodsId);
   //设置秒杀完成
   public   void   setGoodsOver(Long  goodsId);
   public   void  reset(List<GoodsVo > goodsVoList);

   public     boolean  checPath(MiaoshaUser user,long  goodsId,String path);

   public   String  createMiaoshaPath(MiaoshaUser  user,long   gooodsId);

   public BufferedImage  createVerifyCode(MiaoshaUser  user,long  goodsId);
}
