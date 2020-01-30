package com.test;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

 

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        int status = 0;

        if (status == 0) {

            logger.info("status:{}", status);

        } else {

            logger.info("status:{}", status);

        }

        logger.info("end!");
        logger.debug("debug");
        logger.debug("debug","msg"+Thread.currentThread());
        logger.error("error","error_msg");
        int   test_id=123456;
          String    symbol="ssss";
        logger.debug("Processing trade with id: {} and symbol : {} ", test_id, symbol);

    }

}
