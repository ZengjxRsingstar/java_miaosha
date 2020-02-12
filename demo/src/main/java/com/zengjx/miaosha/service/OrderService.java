package com.zengjx.miaosha.service;

import com.zengjx.miaosha.domain.MiaoshaOrder;
import com.zengjx.miaosha.domain.OrderInfo;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/31  23:41
 * @Version V1.0
 */
public interface OrderService {

    public   long   insertOrder(OrderInfo   orderInfo);

    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long id, Long goodsId);
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    void deleteOrders();

    OrderInfo getOrderById(long orderId);
}
