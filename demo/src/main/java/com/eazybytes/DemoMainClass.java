package com.eazybytes;

import com.eazybytes.beans.Vehicle;
import com.eazybytes.config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DemoMainClass {
    public static void main(String[] args) {

        Vehicle vehicle = new Vehicle();
        vehicle.setName("BMW");
        System.out.println("Vehicle name from non spring context is: " + vehicle.getName());

        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        var veh = context.getBean(Vehicle.class);
        System.out.println("Vehicle name from spring context: " + veh.getName());

        var greet = context.getBean(String.class);
        System.out.println(greet);

        var greet1 = (String)context.getBean("greeting");
        System.out.println(greet1);

    }
}
