package com.eazybytes.ex2.config;

import com.eazybytes.ex2.beans.Vehicle;
import org.springframework.context.annotation.*;

@Configuration
@Import({AnotherProjectConfig.class})
public class ProjectConfig {

    // can mark primary so that program can access in case of ambiguity
    @Primary
    @Bean(name="teslaVehicle")
    Vehicle vehicle1() {
        var veh = new Vehicle();
        veh.setName("Tesla");
        return veh;
    }

    @Bean(value="audiVehicle")
    Vehicle vehicle2() {
        var veh = new Vehicle();
        veh.setName("Audi");
        return veh;
    }

    // can provide multiple bean name and can be accessed through any of these name
    @Bean({"hondaVehicle", "anotherVehicle"})
    @Description("This is a vehicle class bean")
    Vehicle vehicle3() {
        var veh = new Vehicle();
        veh.setName("Honda");
        return veh;
    }
}