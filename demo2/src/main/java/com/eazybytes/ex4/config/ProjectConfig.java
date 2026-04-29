package com.eazybytes.ex4.config;

import com.eazybytes.ex4.beans.Person;
import com.eazybytes.ex4.beans.Vehicle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


public class ProjectConfig {
    @Bean
    Vehicle vehicle(){
        Vehicle veh = new Vehicle();
        veh.setName("Tesla");
        return veh;
    }

//    @Bean
//    Person person(){
//        Person p = new Person();
//        p.setName("Suman");
//        p.setVehicle(vehicle());
//        return p;
//    }

    @Bean
    Person person(Vehicle vehicle){
        Person p = new Person();
        p.setName("Suman");
        p.setVehicle(vehicle);
        return p;
    }

}