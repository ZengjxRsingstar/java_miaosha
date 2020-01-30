package com.zengjx.miaosha.exception;

import com.zengjx.miaosha.result.CodeMsg;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/24  16:50
 * @Version V1.0
 */
public class RedisException   extends   RuntimeException {
    private static final long serialVersionUID = 1L;
    private CodeMsg cm;
    public RedisException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }
    public CodeMsg getCm() {
        return cm;
    }
}
