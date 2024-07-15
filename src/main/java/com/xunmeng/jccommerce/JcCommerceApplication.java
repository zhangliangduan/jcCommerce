package com.xunmeng.jccommerce;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xunmeng.jccommerce.mapper")
public class JcCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JcCommerceApplication.class, args);
    }

}
