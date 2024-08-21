package com.quizmaster.models;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterTeacherRequestModel {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;


    @NotEmpty(message = "school cannot be empty")
    private String school;

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 6,message = "password must contain at least 6 characters long")
    private String password;
}
