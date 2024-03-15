package com.quizmaster.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequestModel {

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    private String password;

}