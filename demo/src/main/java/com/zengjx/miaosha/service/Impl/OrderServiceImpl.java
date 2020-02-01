package com.zengjx.miaosha.service.Impl;

import com.zengjx.miaosha.dao.OrderDao;
import com.zengjx.miaosha.domain.MiaoshaOrder;
import com.zengjx.miaosha.domain.OrderInfo;
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

  public  long   insertOrder(OrderInfo   orderInfo){
      logger.info("OrderServiceImpl insertOrder");
     return  orderDao.insert(orderInfo);
  }

    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long id, Long goodsId) {

        logger.info("OrderServiceImpl getMiaoshaOrderByUserIdGoodsId   id ="+id +" goodsId ="+goodsId);

        return  orderDao.getMiaoshaOrderByUserIdGoodsId(id,goodsId);
    }
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder){
      return   orderDao.insertMiaoshaOrder(miaoshaOrder);
    }

}
