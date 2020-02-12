package com.zengjx.miaosha.rabbitmq;

import com.zengjx.miaosha.domain.MiaoshaOrder;
import com.zengjx.miaosha.domain.MiaoshaUser;
import com.zengjx.miaosha.exception.GlobalException;
import com.zengjx.miaosha.redis.RedisService;
import com.zengjx.miaosha.service.GoodsService;
import com.zengjx.miaosha.service.MiaoshaService;
import com.zengjx.miaosha.service.OrderService;
import com.zengjx.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/2/7  17:54
 * @Version V1.0
 */
@Service
public class MQReceiver {

    @Autowired
    private RedisService  redisService;
    @Autowired
    private GoodsService   goodsService;
    @Autowired
    private OrderService   orderService;
    @Autowired
    private MiaoshaService   miaoshaService;
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues=MQConfig.QUEUE)
    public    void   recevice(String   message){

     log.info("recevice "+message);
        System.out.println("recevice");
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public   void   topic_recevice1(String   message)
    {
     log.info("topic_recevice1",message);

    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public   void   topic_recevice2(String   message)
    {
        log.info("topic_recevice2",message);

    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)

      public  void   header_queue(String  message){
        log.info("header_queue"+message);

    }

    @RabbitListener(queues = MQConfig.FANOUT_EXCHANGE)
    public   void    fanout(String   message){

        log.info("fanout_queue"+message);

    }


    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public   void    receiver(String   message)  throws  GlobalException{
        log.info("miaosha_recev "+message);
        MiaoshaMessge  mm =RedisService.stringToBean(message,MiaoshaMessge.class);
        MiaoshaUser  user =mm.getUser();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(mm.getGoodsId());
        int stock =goodsVo.getStockCount();
        if(stock<=0){
            return;
        }
        //判断是否已经秒杀到了
        MiaoshaOrder  order =orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsVo.getId());
        if(order==null){
            miaoshaService.miaosha(user,goodsVo);
        }


    }




}
