package com.taptalk.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FirebaseProxyConfig {

    // Firebase와 통신할 WebClient 설정
    // 필요한 경우에만 백엔드 서비스에서 Firebase와 통신할 때 사용
    @Bean
    public WebClient firebaseWebClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
} 