package com.quizmaster.models;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class TeacherRegisterRequestModel {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "firstname cannot be empty")
    private String firstName;

    @NotEmpty(message = "lastname cannot be empty")
    private String lastName;

    @NotEmpty(message = "school cannot be empty")
    private String school;
}
