package com.wq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wq
 */
@SpringBootApplication
@EnableAsync
public class HairyChildrenSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run (HairyChildrenSpringbootApplication.class, args);
    }

}
