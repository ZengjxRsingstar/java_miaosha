package com.zengjx.miaosha.result;

import com.zengjx.miaosha.redis.KeyPrefix;

/**
 * @ClassName CodeMsg
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/13  15:40
 * @Version V1.0
 * @function: 消息
 */
public class CodeMsg {
    //用户模块
    public static final CodeMsg GETBYID_ERROR = new CodeMsg(500700,"用户查询失败") ;
    public static final CodeMsg USER_PASSWRPD_ERROR =new CodeMsg(500701,"用户查询失败") ;
    public static final CodeMsg MIAO_SHA_ORDER = new CodeMsg(500702,"该用户秒杀订单已经存在");
    private int code;
    private String msg;

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");



    //商品模块 5003XX

    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST =new CodeMsg(500400,"秒杀订单不存在");
    //秒杀模块 5005XX
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");
    public static CodeMsg MIAO_SHA_ERROR = new CodeMsg(500501, "秒杀异常");
    //redis 模块错误
    public static CodeMsg REDIS_ERROR = new CodeMsg(500615, "REDIS错误");
    private CodeMsg( ) {
    }

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

//    public Object fillArgs(Object... args ) {
//      int   code =this.code;
//     String   message =String.format(this.msg,args);
//      return   new CodeMsg(code,message);
//    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

