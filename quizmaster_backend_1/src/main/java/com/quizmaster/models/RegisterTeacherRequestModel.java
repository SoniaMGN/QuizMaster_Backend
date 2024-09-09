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

    @NotEmpty(message = "gradelevel cannot be empty")
    private String gradeLevel;

    @NotEmpty(message = "homeroomClass cannot be empty")
    private String homeroomClass;

    @NotEmpty(message = "yearsOfExperience cannot be empty")
    private int yearsOfExperience;

    @NotEmpty(message = "classroomNumber cannot be empty")
    private String classroomNumber;

    private String contactNumber;

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 6,message = "password must contain at least 6 characters long")
    private String password;
}
