package com.eazybytes.ex4.beans;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


public class Vehicle  {
    private String name;

    public Vehicle(){
        System.out.println("Vehicle Bean is created");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Vehicle " +
                "name: " + name  +
                '}';
    }
}
