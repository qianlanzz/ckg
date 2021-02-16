package com.qianlan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.qianlan.mapper")
@SpringBootApplication
public class KgApplication {

    public static void main(String[] args) {
        SpringApplication.run(KgApplication.class, args);
    }

}
