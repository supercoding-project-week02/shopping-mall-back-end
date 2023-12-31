package com.supercoding.shoppingmallbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableCaching
@EnableAsync
public class ShoppingMallBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMallBackEndApplication.class, args);
    }

}
