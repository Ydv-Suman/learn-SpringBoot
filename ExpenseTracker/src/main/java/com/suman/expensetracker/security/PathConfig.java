package com.suman.expensetracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathConfig {

    @Bean("publicPaths")
    public List<String> publicPaths() {
        return List.of(
                "/",
                "/index.html",
                "/styles.css",
                "/app.js",
                "/favicon.ico",
                "/assets/**",
                "/auth/login",
                "/auth/register",
                "/csrf-token"
        );
    }
    @Bean("securePaths")
    public List<String> securePaths() {
        return List.of(
                "/categories/**",
                "/expense-lists/**"
        );
    }

}
