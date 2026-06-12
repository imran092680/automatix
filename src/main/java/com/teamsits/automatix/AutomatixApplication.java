package com.teamsits.automatix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EntityScan(basePackageClasses = User.class)
@EnableScheduling
@EnableAsync
@EnableCaching
@SpringBootApplication
public class AutomatixApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutomatixApplication.class, args);
    }
}
