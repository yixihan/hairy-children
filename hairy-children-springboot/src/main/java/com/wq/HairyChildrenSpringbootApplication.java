package com.wq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author wq
 */
@SpringBootApplication
@EnableOpenApi
@EnableAsync
public class HairyChildrenSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run (HairyChildrenSpringbootApplication.class, args);
    }

}
