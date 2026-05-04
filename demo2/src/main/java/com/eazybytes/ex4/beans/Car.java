package com.eazybytes.ex4.beans;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Car {
    private String name;

    // Field injection
    // @Autowired
    private Engine engine;

    // Constructor injection
    @Autowired
    public Car(Engine engine){
        this.engine = engine;
        System.out.println("Car Bean is created");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Engine getEngine() {
        return engine;
    }

    // setter injection
    // @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @PostConstruct
    public void initialize(){
        this.name = "KIA";
    }

    @Override
    public String toString() {
        return "Car " +
                "name: " + name  +
                '}';
    }
}
