package com.test;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/25  17:18
 * @Version V1.0
 */
public   class DeadLoopClass {

    static {
        if(true){

            System.out.println(Thread.currentThread()+"init  DeadLoopClass");
            while (true){

            }
        }


    }

    public static void main(String[] args) {

        Runnable  script =new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread()+"start");
                DeadLoopClass  dlc =new DeadLoopClass() ;
                System.out.println(Thread.currentThread()+" end  over");
            }
        };

         Thread  thread1 =new Thread(script);
         Thread  thread2 =new Thread(script);
         thread1.start();
         thread2.start();
    }

}
