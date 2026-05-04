package com.eazybytes.ex4;

import com.eazybytes.ex4.beans.Car;
import com.eazybytes.ex4.beans.Engine;
import com.eazybytes.ex4.beans.Person;
import com.eazybytes.ex4.beans.Vehicle;
import com.eazybytes.ex4.config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Example4 {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
        var vehicle = context.getBean(Vehicle.class);
        var person = context.getBean(Person.class);

        System.out.println("Person name from spring context is: " + person.getName());
        System.out.println("Vehicle name from spring context is: " +  vehicle.getName());
        System.out.println("Vehicle that is owned by " + person.getName() + " is: " + person.getVehicle());

        var car = context.getBean(Car.class);
        var engine = context.getBean(Engine.class);

        System.out.println("Car name from spring context is: " + car.getName());
        System.out.println("Engine name from spring context is: " +  engine.getName());
        System.out.println("Car that is  " + car.getName() + " has: " + car.getEngine());

    }
}