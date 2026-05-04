package com.eazybytes.ex5.beans;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("espresso")
// @Primary
public class Espresso implements Coffee{
    @Override
    public String makeCoffee(){
        return "Espresso Coffee";
    }
}
