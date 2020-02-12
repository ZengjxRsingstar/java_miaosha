package com.zengjx.miaosha.rabbitmq;

import com.zengjx.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.MalformedInputException;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/2/7  17:53
 * @Version V1.0
 */
@Service
public class MQSender {
    private static Logger log = LoggerFactory.getLogger(MQSender.class);
    @Autowired
   private AmqpTemplate   amqpTemplate;
    public   void   send(Object   message)
    {

       amqpTemplate.convertAndSend(MQConfig.QUEUE,message);
    }

    public void sendTopic(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send topic message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
	}


    	public   void sendFanout(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send fanout message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
	}

	public    void   sendHeader(Object   message){
     String  msg =RedisService.beanToString(message);
     log.info("send topic message:"+msg);
        MessageProperties properties=new MessageProperties();
        properties.setHeader("header1","value1");
        properties.setHeader("header2","value2");
        Message  obj=new Message(msg.getBytes(),properties);
      amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",msg);
    }

    public void sendMiaoshaMessage(MiaoshaMessge miaoshaMessge) {

        String  msg= RedisService.beanToString(miaoshaMessge);
        log.info("send miaosha"+msg);
       amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);

    }
}
