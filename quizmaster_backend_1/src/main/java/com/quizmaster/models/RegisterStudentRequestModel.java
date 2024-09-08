package com.quizmaster.models;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
public class RegisterStudentRequestModel {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "school cannot be empty")
    private String school;

    @NotEmpty(message = "firstname cannot be empty")
    private String firstName;

    @NotEmpty(message = "lastname cannot be empty")
    private String lastName;

    @NotEmpty(message = "grade cannot be empty")
    private String grade;

    @NotEmpty(message = "date of birth cannot be empty")
    private String dateOfBirth;

    @NotEmpty(message = "address of birth cannot be empty")
    private String address;

    @NotEmpty(message = "name cannot be empty")
    private String registrationDate;

    @NotEmpty(message = "name cannot be empty")
    private String parentName;

    @NotEmpty(message = "name cannot be empty")
    private String parentContact;


}
