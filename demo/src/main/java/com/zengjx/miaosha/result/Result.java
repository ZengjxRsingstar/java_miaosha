package com.zengjx.miaosha.result;

/**
 * @ClassName Result
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/13  15:41
 * @Version V1.0
 * @function : 返回结果
 */
public class Result<T> {
    private int code;//代码
    private String msg;//信息
    private T data;//数据

    public Result() {
    }

    /**
     * 成功时候的调用
     * */
    public static <T> Result<T> success(T data){
        return new  Result<T>(data);
    }

    /**
     * 失败时候的调用
     * */
    public static <T> Result<T> error(CodeMsg cm){
        return new  Result<T>(cm);
    }

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg cm) {
        if(cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public T getData() {
        return data;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}