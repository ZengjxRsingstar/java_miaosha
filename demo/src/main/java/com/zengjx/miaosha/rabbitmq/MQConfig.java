package com.zengjx.miaosha.rabbitmq;

import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/2/7  17:04
 * @Version V1.0
 */
@Configuration
public class MQConfig {

  public   static   final  String QUEUE ="queue";
  public static final String TOPIC_QUEUE1 = "topic.queue11";
  public static final String TOPIC_QUEUE2 = "topic.queue21";
  public static final String HEADER_QUEUE = "header.queue";
  public static final String TOPIC_EXCHANGE = "topicExchage";
  public static final String FANOUT_EXCHANGE = "fanoutExchage";
  public static final String HEADERS_EXCHANGE = "headersExchage";

  public   static   final  String MIAOSHA_QUEUE="miaosha_queue";

  @Bean
  public Queue   miaoshaoQueue(){
      return  new Queue(MIAOSHA_QUEUE,true);
  }
  /***
     * Direct 模式交换机Exchange
     *
     */
    @Bean
    public Queue  queue(){

        return   new Queue(QUEUE,true);

    }
    @Bean
   public   Queue   topicQueue1(){
      return  new   Queue(TOPIC_QUEUE1,true);
    }
  @Bean
  public   Queue   topicQueue2(){
    return  new   Queue(TOPIC_QUEUE2,true);
  }
  @Bean
  public TopicExchange  topicExchange(){
      return  new TopicExchange(TOPIC_EXCHANGE);
  }
  @Bean
  public Binding    topicBingding1(){
      return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
  }
  @Bean
  public Binding    topicBingding2(){
    return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
  }
  @Bean
  public FanoutExchange fanoutExchage(){
    return new FanoutExchange(FANOUT_EXCHANGE);
  }
  @Bean
  public  Binding  FanoutBinding1(){
    return   BindingBuilder.bind(topicQueue1()).to(fanoutExchage());

  }
  @Bean
  public Binding FanoutBinding2() {
    return BindingBuilder.bind(topicQueue2()).to(fanoutExchage());
  }
 @Bean
  public HeadersExchange   headersExchange(){
      return  new HeadersExchange(HEADERS_EXCHANGE);
 }
 @Bean
 public Queue headerQueue1(){
    return  new Queue(HEADER_QUEUE,true);
 }

 @Bean
  public   Binding   headerBinding(){
   Map<String,Object>  map =new HashMap<>();
   map.put("header1","value1");
   map.put("header2","value2");
   return  BindingBuilder.bind(headerQueue1()).to(headersExchange()).whereAll(map).match();

 }

}
