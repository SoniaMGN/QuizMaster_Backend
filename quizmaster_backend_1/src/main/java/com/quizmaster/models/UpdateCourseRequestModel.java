package com.quizmaster.models;



import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateCourseRequestModel {

    @NotEmpty(message = "previous course code cannot be empty.")
    private String oldCourseCode;

    private String newCourseName;

    private String newDescription;

    private int newCapacity;

    private String semester;
}
