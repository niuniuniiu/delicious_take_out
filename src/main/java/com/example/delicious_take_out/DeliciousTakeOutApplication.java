package com.example.delicious_take_out;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.zip.DataFormatException;

//lombok提供的注解，操作在idea中的控制台中打印的日志信息,有些注解可以省略get/set可以省略等
@Slf4j
@SpringBootApplication
@MapperScan("com.example.delicious_take_out.mapper")
@ServletComponentScan
public class DeliciousTakeOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliciousTakeOutApplication.class, args);
        log.info("项目启动成功");
    }

}
