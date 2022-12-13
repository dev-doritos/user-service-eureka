package com.example.userservice.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-service/users")
public class UserServiceController {

    Environment environment;

    public UserServiceController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping(value = "/")
    public void toUserAllSelect() {

    }

    @PostMapping(value = "/")
    public void toUserRegister() {

    }

    @GetMapping(value = "/{id}")
    public void toUserSelect(@PathVariable(value = "id") String id) {

    }

    @PutMapping(value = "/{id}")
    public void toUserUpdate(@PathVariable(value = "id") String id) {

    }

    @DeleteMapping(value = "/{id}")
    public void toUserDelete(@PathVariable(value = "id") String id) {

    }

    @GetMapping(value = "/health-check")
    public String healthCheck() {
        return environment.getProperty("spring.application.name") + " Connected! "
                + environment.getProperty("greeting.message");
    }
}
