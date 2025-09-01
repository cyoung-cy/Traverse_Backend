package com.taptalk.aimap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AiMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiMapApplication.class, args);
    }

}
