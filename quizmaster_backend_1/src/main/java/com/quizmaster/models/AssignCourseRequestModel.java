package com.quizmaster.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
public class AssignCourseRequestModel {
        @NotNull(message = "Course code cannot be null")
        private String courseCode;

        @NotNull(message = "Teacher ID cannot be null")
        private String teacherId;
}
