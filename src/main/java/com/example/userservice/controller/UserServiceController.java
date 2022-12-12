package com.example.userservice.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-service")
public class UserServiceController {

    Environment environment;

    public UserServiceController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping(value = "/health-check")
    public String healthCheck() {
        return environment.getProperty("greeting.message");
    }
}
