package com.quizmaster.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
public class RemoveStudentRequestModel {
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;
}
