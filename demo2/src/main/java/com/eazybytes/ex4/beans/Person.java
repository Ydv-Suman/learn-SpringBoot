package com.eazybytes.ex4.beans;

public class Person {
    private String name;
    private Vehicle vehicle;

    public Person(){
        System.out.println("Person Bean is created");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
