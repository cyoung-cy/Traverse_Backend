package com.taptalk.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-auth", r -> r
                        .path("/api/auth/**", "/api/users/**", "/api/admins/**", "/api/reports/**", "/api/posts/**", "/api/notifications/**", "/api/firestore/**")
                        .uri("lb://USER-AUTH"))
                .route("ai-map", r -> r
                        .path("/api/quests/**")
                        .uri("lb://AI-MAP"))
                .route("message-service", r -> r
                        .path("/api/messages/**")
                        .uri("lb://MESSAGE-SERVICE"))
                .route("notification-service", r -> r
                        .path("/api/fcm/**")
                        .uri("lb://NOTIFICATION-SERVICE"))
                .build();
    }
} 