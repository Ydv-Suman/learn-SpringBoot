package com.eazybytes.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    // @GetMapping("/hello")
    @RequestMapping(path = "/home", method= {RequestMethod.GET, RequestMethod.POST})
    public String sayHello() {
        return "Hello World from Spring Boot";
    }
}
