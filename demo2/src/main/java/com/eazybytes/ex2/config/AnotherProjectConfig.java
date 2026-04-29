package com.eazybytes.ex2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnotherProjectConfig {

    @Bean
    String greeting(){
        return "Hello! I am Suman Yadav.";
    }

    @Bean
    Integer favoriteNumber(){
        return 7;
    }
}
