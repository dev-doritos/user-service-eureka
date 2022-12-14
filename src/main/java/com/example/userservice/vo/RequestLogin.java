package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {

    @NotNull(message = "Email cannot be null")
    @Email
    private String email;
    @Size(min = 8, message = "Password must be equal or grater than 8 character")
    private String password;
}
