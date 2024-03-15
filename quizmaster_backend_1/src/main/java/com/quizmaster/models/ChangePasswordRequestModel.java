package com.quizmaster.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@Builder
public class ChangePasswordRequestModel {

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 6,message = "password must contain at least 6 characters long")
    private String password;

    @NotEmpty(message = "confirm password cannot be empty")
    @Size(min = 6,message = "password must contain at least 6 characters long")
    private String confirmPassword;


}
