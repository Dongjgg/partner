package com.dj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dj.mapper")
public class PartnerBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartnerBackApplication.class, args);
    }

}
