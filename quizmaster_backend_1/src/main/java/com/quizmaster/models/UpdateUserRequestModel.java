package com.quizmaster.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@Builder
public class UpdateUserRequestModel {

    @NotEmpty(message = "firstname cannot be empty")
    private String firstName;

    @NotEmpty(message = "lastname cannot be empty")
    private String lastName;


}

