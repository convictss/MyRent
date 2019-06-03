package com.convict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Author Convict
 * @Date 2019/2/27 18:33
 */

//@SpringBootApplication
public class MyRentWarApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MyRentWarApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MyRentWarApplication.class, args);
    }

}
