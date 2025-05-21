package com.eliska.cattoprojectweb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CattoProjectWebApplication {

    public static final Logger logger = LoggerFactory.getLogger(CattoProjectWebApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CattoProjectWebApplication.class, args);
    }

    public static void MyLog(String message) {

        System.out.printf("||==//  %s   \\\\==||\n", message);
    }

    public static void MyLogLogger(String message) {

        logger.info("||==//  %s   \\\\==||\n");
    }


}
