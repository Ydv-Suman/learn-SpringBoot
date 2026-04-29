package com.eazybytes.ex1;

import com.eazybytes.ex1.beans.Vehicle;
import com.eazybytes.ex1.config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Example1 {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        var veh1 = context.getBean("vehicle1", Vehicle.class);
        System.out.println("Vehicle name from spring context: " + veh1.getName());

        var veh2 = context.getBean("vehicle2", Vehicle.class);
        System.out.println("Vehicle name from spring context: " + veh2.getName());

        var veh3 = (Vehicle)context.getBean("vehicle3");
        System.out.println("Vehicle name from spring context: " + veh3.getName());

    }
}