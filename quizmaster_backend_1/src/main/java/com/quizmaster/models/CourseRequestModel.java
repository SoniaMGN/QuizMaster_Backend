package com.quizmaster.models;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class CourseRequestModel {

    @NotEmpty(message = "name cannot be empty")
    private String courseName;

    @NotEmpty(message = "description cannot be empty")
    private String description;

    @NotEmpty(message = "capacity cannot be empty")
    private int capacity;

    @NotEmpty(message = "semester cannot be empty")
    private String semester;
}
