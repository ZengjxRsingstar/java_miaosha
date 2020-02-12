package com.zengjx.miaosha.service.Impl;

import com.zengjx.miaosha.dao.GoodsDao;
import com.zengjx.miaosha.domain.MiaoshaGoods;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao   goodsDao;
    @Override
    public List<GoodsVo> listGoods() {
        List<GoodsVo> goodsVos = goodsDao.listGoodsVo();
        return goodsVos;
    }
    @Override
    public GoodsVo getGoodsVoByGoodsId(Long goodsId) {
        GoodsVo goodsVo = goodsDao.getGoodsVoByGoodsId(goodsId);
        return goodsVo;
    }
    /***
     *   扣减库存
     * @param goods
     * @return
     */
    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods  miaoshaGoods= new MiaoshaGoods();
        miaoshaGoods.setId(goods.getId());
        miaoshaGoods.setGoodsId(goods.getId());
        miaoshaGoods.setStockCount(goods.getStockCount());
        int stock = goodsDao.reduceStock(miaoshaGoods);
         return  stock>0;
    }
    /**
     * 恢复库存
     * @param goodsList
     */
    public void resetStock(List<GoodsVo> goodsList) {
        for (GoodsVo goodsVo : goodsList) {
            MiaoshaGoods  goods =new MiaoshaGoods();
            goods.setStockCount(goodsVo.getStockCount());
            goodsDao.resetStock(goods);
        }
    }
}
