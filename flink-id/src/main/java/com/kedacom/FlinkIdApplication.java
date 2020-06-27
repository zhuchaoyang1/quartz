package com.kedacom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * jobdetail可以对应多个trigger，但是一个trigger只能对应一个job
 */
@SpringBootApplication
@ComponentScan(basePackages = {"config", "com.kedacom"})
public class FlinkIdApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlinkIdApplication.class, args);
    }

}
