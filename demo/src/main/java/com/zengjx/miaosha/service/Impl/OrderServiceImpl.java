package com.zengjx.miaosha.service.Impl;

import com.zengjx.miaosha.dao.OrderDao;
import com.zengjx.miaosha.domain.MiaoshaOrder;
import com.zengjx.miaosha.domain.OrderInfo;
import com.zengjx.miaosha.redis.OrderKey;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/31  23:41
 * @Version V1.0
 */
@Service
public class OrderServiceImpl  implements OrderService {
    private   static Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class) ;
    @Autowired
    private OrderDao    orderDao;
    @Autowired
    private RedisService  redisService;

  public  long   insertOrder(OrderInfo   orderInfo){
      logger.info("OrderServiceImpl insertOrder");

     return  orderDao.insert(orderInfo);
  }
    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long id, Long goodsId) {

        logger.info("OrderServiceImpl getMiaoshaOrderByUserIdGoodsId   id ="+id +" goodsId ="+goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+id+"_"+goodsId, MiaoshaOrder.class);
    }
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder){
      return   orderDao.insertMiaoshaOrder(miaoshaOrder);
    }
    @Override
    public void deleteOrders() {
        orderDao.deleteOrders();
    }

    @Override
    public OrderInfo getOrderById(long orderId) {


        return orderDao.getOrderById(orderId);
    }

}
