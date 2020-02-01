package com.zengjx.miaosha.service;

import com.zengjx.miaosha.vo.GoodsVo;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/30  20:46
 * @Version V1.0
 */
public interface GoodsService {

public      List<GoodsVo>    listGoods();

 public    GoodsVo   getGoodsVoByGoodsId(Long goodsId);
 public boolean reduceStock(GoodsVo goods);
}
