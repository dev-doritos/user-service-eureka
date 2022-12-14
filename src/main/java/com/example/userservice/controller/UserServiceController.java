package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-service")
public class UserServiceController {

    private final Environment environment;
    private final UserService userService;

    public UserServiceController(Environment environment, UserService userService) {
        this.environment = environment;
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        // mapping 엄격하게 설정
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        // JSON to UserDTO
        UserDTO userDTO = mapper.map(user, UserDTO.class);

        // userService.createUser 호출
        userDTO = userService.createUser(userDTO);

        // UserDTO to JSON
        ResponseUser responseData = mapper.map(userDTO, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @GetMapping(value = "/users")
    public void toUserAllSelect() {

    }

    @GetMapping(value = "/users/{id}")
    public void toUserSelect(@PathVariable(value = "id") String id) {

    }

    @PutMapping(value = "/users/{id}")
    public void toUserUpdate(@PathVariable(value = "id") String id) {

    }

    @DeleteMapping(value = "/users/{id}")
    public void toUserDelete(@PathVariable(value = "id") String id) {

    }

    @GetMapping(value = "/health-check")
    public String healthCheck() {
        return environment.getProperty("local.server.port") + " Connected! "
                + environment.getProperty("greeting.message");
    }
}
