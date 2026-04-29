package com.eazybytes.ex2;

import com.eazybytes.ex2.beans.Vehicle;
import com.eazybytes.ex2.config.AnotherProjectConfig;
import com.eazybytes.ex2.config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Example2 {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        var veh1 = context.getBean("teslaVehicle", Vehicle.class);
        System.out.println("Vehicle name from spring context: " + veh1.getName());

        var veh2 = context.getBean("audiVehicle", Vehicle.class);
        System.out.println("Vehicle name from spring context: " + veh2.getName());

        var veh3 = (Vehicle)context.getBean("anotherVehicle");
        System.out.println("Vehicle name from spring context: " + veh3.getName());

        var veh = context.getBean(Vehicle.class);
        System.out.println("Vehicle name from spring context: " + veh.getName());

        var greet = (String)context.getBean(String.class);
        System.out.println("Greeting: " + greet);

    }
}