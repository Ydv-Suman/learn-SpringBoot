package com.eazybytes.ex3;

import com.eazybytes.ex3.beans.Vehicle;
import com.eazybytes.ex3.config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Example3 {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        var veh1 = context.getBean(Vehicle.class);
        System.out.println("Vehicle name from spring context: " + veh1.getName());

        veh1.sayHello();
        context.close();


    }
}