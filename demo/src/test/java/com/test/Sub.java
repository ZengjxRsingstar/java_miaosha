package com.test;

import static com.test.Parent.A;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/25  13:02
 * @Version V1.0
 */
 class  Parent{
    public   static  int A=1;
    static {
        A=2;
        System.out.println("Parent.....");
    }

}

public class Sub {

 public   static   int B=A;

    public static void main(String[] args) {
        System.out.println(Sub.B);
    }

 }
